// Configuración para vistaWeb.js
urlIniciarVista = "/propietario/vistaConectada";
urlCierreVista = "/propietario/vistaCerrada";
// Configuración SSE para notificaciones en tiempo real
urlRegistroSSE = "/propietario/registrarSSE";

// Event listeners - asegurar que el DOM esté listo
document.addEventListener("DOMContentLoaded", function () {
  const btnBorrarNotificaciones = document.getElementById(
    "btnBorrarNotificaciones"
  );
  const btnSalir = document.getElementById("btnSalir");

  if (btnBorrarNotificaciones) {
    btnBorrarNotificaciones.addEventListener("click", borrarNotificaciones);
  }

  if (btnSalir) {
    btnSalir.addEventListener("click", salir);
  }
});

function borrarNotificaciones() {
  // Call backend to delete all notifications
  submit("/propietario/notificaciones/borrar", "", "POST");
}

function procesarErrorSubmit(status, text) {
  console.error(
    "Error capturado por vistaWeb.js - Status:",
    status,
    "Text:",
    text
  );

  if (status === 401) {
    window.location.href = window.location.origin + "/login.html";
    return;
  }

  // Try to parse error as JSON
  try {
    const errorObj = JSON.parse(text);
    alert(errorObj.message || text);
  } catch (e) {
    alert(text || "Error en la operación");
  }
}

function salir() {
  submit("/login/logoutPropietario", "", "POST");
}

// Handlers para vistaWeb.js
window["mostrar_nombrePropietario"] = function (parametro) {
  console.log("Propietario conectado:", parametro);
};

// Variables para almacenar datos temporalmente
let transitosDTOsActuales = [];
let propietarioActual = null;

window["mostrar_propietario"] = function (parametro) {
  console.log("Datos del propietario:", parametro);
  propietarioActual = parametro;
  renderizarDatosPropietario(parametro, transitosDTOsActuales);
};

window["mostrar_transitos"] = function (parametro) {
  console.log("Tránsitos DTOs recibidos:", parametro);
  transitosDTOsActuales = parametro || [];
  // Si ya tenemos el propietario renderizado, actualizar los transitos
  if (propietarioActual) {
    renderizarDatosPropietario(propietarioActual, transitosDTOsActuales);
  }
};

window["mostrar_paginaLogin"] = function (parametro) {
  window.location.href = "/login.html";
};

window["mostrar_notificaciones"] = function (parametro) {
  console.log("Notificaciones recibidas (SSE):", parametro);
  renderizarNotificacionesDesdeBackend(parametro);
};

window["mostrar_mensaje"] = function (parametro) {
  console.log("Mensaje recibido:", parametro);
  // Could show a toast notification here if needed
};

function renderizarDatosPropietario(propietario, transitosDTOs = []) {
  console.log("Renderizando datos del propietario:", propietario);
  console.log("Tránsitos DTOs:", transitosDTOs);

  try {
    // Actualizar resumen
    const resumenContainer = document.getElementById("resumenContainer");

    if (!resumenContainer) {
      console.error("No se encontró el contenedor de resumen");
      return;
    }

    const estadoNombre = propietario?.estado?.nombre || "N/A";
    const estadoColor = obtenerColorEstado(estadoNombre);
    const mensajeRestriccion =
      estadoNombre !== "Habilitado"
        ? `<p class="text-xs text-[var(--destructive)] mt-2">⚠️ ${obtenerMensajeEstado(
            propietario
          )}</p>`
        : "";

    resumenContainer.innerHTML = `
      <div class="bg-[var(--background)] border-l-4 border-l-[var(--primary)] border border-[var(--border)] p-4">
        <p class="text-xs text-[var(--muted-foreground)] mb-2 uppercase tracking-wider">Nombre completo</p>
        <p class="text-base font-semibold text-[var(--foreground)]">${
          propietario?.nombre || "N/A"
        }</p>
        <p class="text-xs text-[var(--muted-foreground)] mt-1">CI: ${
          propietario?.ci || "N/A"
        }</p>
      </div>
      <div class="bg-[var(--background)] border-l-4 ${estadoColor} border border-[var(--border)] p-4">
        <p class="text-xs text-[var(--muted-foreground)] mb-2 uppercase tracking-wider">Estado</p>
        <p class="text-base font-semibold text-[var(--foreground)]">${estadoNombre}</p>
        ${mensajeRestriccion}
      </div>
      <div class="bg-[var(--background)] border-l-4 border-l-[var(--chart-3)] border border-[var(--border)] p-4">
        <p class="text-xs text-[var(--muted-foreground)] mb-2 uppercase tracking-wider">Saldo actual</p>
        <p class="text-base font-semibold text-[var(--foreground)]">$ ${(
          propietario?.saldo || 0
        ).toFixed(2)}</p>
        <p class="text-xs text-[var(--muted-foreground)] mt-1">Alerta: $ ${(
          propietario?.saldoMinAlerta || 0
        ).toFixed(2)}</p>
      </div>
    `;

    // Renderizar vehículos usando DTOs
    renderizarVehiculos(propietario?.vehiculo || [], transitosDTOs);

    // Renderizar bonificaciones
    renderizarBonificaciones(propietario?.asignaciones || []);

    // Renderizar tránsitos usando DTOs
    renderizarTransitos(transitosDTOs);

    // Renderizar notificaciones
    renderizarNotificacionesDesdeBackend(propietario?.notificaciones || []);
  } catch (error) {
    console.error("Error al renderizar datos del propietario:", error);
  }
}

function obtenerColorEstado(estado) {
  switch (estado) {
    case "Habilitado":
      return "border-l-[var(--chart-2)]";
    case "Deshabilitado":
      return "border-l-[var(--destructive)]";
    case "Suspendido":
      return "border-l-[var(--chart-4)]";
    case "Penalizado":
      return "border-l-[var(--chart-5)]";
    default:
      return "border-l-[var(--muted)]";
  }
}

function obtenerMensajeEstado(propietario) {
  const estado = propietario.estado.nombre;
  switch (estado) {
    case "Deshabilitado":
      return "No puede realizar ninguna operación en el sistema";
    case "Suspendido":
      return "No puede realizar tránsitos";
    case "Penalizado":
      return "Las bonificaciones no aplican en tránsitos. No recibe notificaciones";
    default:
      return "";
  }
}

function calcularMontoFinalTransito(transito, transitosDelDia) {
  if (!transito.bono) {
    return transito.tarifa?.monto || 0;
  }

  // Calcular descuento basado en el tipo de bonificación
  const nombreBono = transito.bono?.nombre || "";
  let descuento = 0;

  if (nombreBono === "Exonerados") {
    descuento = 1.0; // 100% descuento
  } else if (nombreBono === "Frecuentes") {
    // Contar tránsitos previos del mismo día, vehículo y puesto
    const fechaTransito = new Date(transito.fecha);
    const transitosPrevios = transitosDelDia.filter((t) => {
      const fechaT = new Date(t.fecha);
      return (
        fechaT < fechaTransito &&
        t.vehiculo?.matricula === transito.vehiculo?.matricula &&
        t.puesto?.nombre === transito.puesto?.nombre &&
        fechaT.toDateString() === fechaTransito.toDateString()
      );
    });
    descuento = transitosPrevios.length > 0 ? 0.5 : 0; // 50% si hay previos
  } else if (nombreBono === "Trabajadores") {
    // Verificar si es día de semana (lunes a viernes)
    const fechaTransito = new Date(transito.fecha);
    const diaSemana = fechaTransito.getDay(); // 0 = domingo, 1 = lunes, ..., 6 = sábado
    descuento = diaSemana >= 1 && diaSemana <= 5 ? 0.8 : 0; // 80% en días laborables
  }

  const montoBase = transito.tarifa?.monto || 0;
  return montoBase * (1.0 - descuento);
}

function renderizarVehiculos(vehiculos, transitosDTOs) {
  console.log("Renderizando vehículos:", vehiculos);
  console.log("Tránsitos DTOs para estadísticas:", transitosDTOs);

  try {
    const tbody = document.getElementById("vehiculosTbody");

    if (!tbody) {
      console.error("No se encontró el tbody de vehículos");
      return;
    }

    if (!vehiculos || vehiculos.length === 0) {
      tbody.innerHTML = `
      <tr>
        <td colspan="5" class="py-4 px-4 text-sm text-[var(--muted-foreground)] text-center">
          No hay vehículos registrados
        </td>
      </tr>
    `;
      return;
    }

    // Calcular estadísticas por vehículo usando DTOs
    const estadisticasPorVehiculo = {};
    if (transitosDTOs && transitosDTOs.length > 0) {
      transitosDTOs.forEach((transitoDTO) => {
        const matricula = transitoDTO.matricula;
        if (matricula) {
          if (!estadisticasPorVehiculo[matricula]) {
            estadisticasPorVehiculo[matricula] = {
              count: 0,
              total: 0,
            };
          }
          estadisticasPorVehiculo[matricula].count++;
          // Extraer monto pagado del DTO (ya viene formateado como "$ XX,XX")
          // Necesitamos convertir de vuelta a número para sumar
          const montoPagadoStr = transitoDTO.montoPagado || "$ 0,00";
          const montoPagado =
            parseFloat(
              montoPagadoStr
                .replace("$", "")
                .replace(/\./g, "")
                .replace(",", ".")
            ) || 0;
          estadisticasPorVehiculo[matricula].total += montoPagado;
        }
      });
    }

    tbody.innerHTML = vehiculos
      .map((v) => {
        const stats = estadisticasPorVehiculo[v.matricula] || {
          count: 0,
          total: 0,
        };
        return `
    <tr>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        v.matricula || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        v.modelo || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        v.color || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        stats.count
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">$ ${stats.total
        .toFixed(2)
        .replace(".", ",")}</td>
    </tr>
  `;
      })
      .join("");
  } catch (error) {
    console.error("Error al renderizar vehículos:", error);
  }
}

function renderizarBonificaciones(asignaciones) {
  console.log("Renderizando bonificaciones:", asignaciones);

  try {
    const tbody = document.getElementById("bonificacionesTbody");

    if (!tbody) {
      console.error("No se encontró el tbody de bonificaciones");
      return;
    }

    if (!asignaciones || asignaciones.length === 0) {
      tbody.innerHTML = `
      <tr>
        <td colspan="3" class="py-4 px-4 text-sm text-[var(--muted-foreground)] text-center">
          No hay bonificaciones asignadas
        </td>
      </tr>
    `;
      return;
    }

    tbody.innerHTML = asignaciones
      .map(
        (a) => `
    <tr>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        a.bonificacion?.nombre || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        a.puesto?.nombre || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--muted-foreground)] border-b border-[var(--border)]">${
        a.fechaAsignacion || "-"
      }</td>
    </tr>
  `
      )
      .join("");
  } catch (error) {
    console.error("Error al renderizar bonificaciones:", error);
  }
}

function renderizarTransitos(transitosDTOs) {
  console.log("Renderizando tránsitos DTOs:", transitosDTOs);

  try {
    const tbody = document.getElementById("transitosTbody");

    if (!tbody) {
      console.error("No se encontró el tbody de tránsitos");
      return;
    }

    if (!transitosDTOs || transitosDTOs.length === 0) {
      tbody.innerHTML = `
      <tr>
        <td colspan="9" class="py-4 px-4 text-sm text-[var(--muted-foreground)] text-center">
          No hay tránsitos realizados
        </td>
      </tr>
    `;
      return;
    }

    // Ordenar tránsitos por fecha (más recientes primero)
    // La fecha viene en formato "dd/MM/yyyy HH:mm:ss"
    const transitosOrdenados = [...transitosDTOs].sort((a, b) => {
      // Parsear fecha desde formato "dd/MM/yyyy HH:mm:ss"
      const parsearFecha = (fechaStr) => {
        const [fechaParte, horaParte] = fechaStr.split(" ");
        const [dia, mes, año] = fechaParte.split("/");
        const [hora, minuto, segundo] = horaParte.split(":");
        return new Date(año, mes - 1, dia, hora, minuto, segundo);
      };
      const fechaA = parsearFecha(a.fecha || "");
      const fechaB = parsearFecha(b.fecha || "");
      return fechaB - fechaA;
    });

    tbody.innerHTML = transitosOrdenados
      .map((dto) => {
        // Extraer fecha y hora del formato "dd/MM/yyyy HH:mm:ss"
        const fechaCompleta = dto.fecha || "";
        const [fechaStr, horaStr] = fechaCompleta.split(" ");

        return `
    <tr>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        dto.puesto || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)] font-mono">${
        dto.matricula || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        dto.categoria || "N/A"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        dto.montoTarifa || "-"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        dto.bonificacion || "Ninguna"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)]">${
        dto.montoBonificacion || "-"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--foreground)] border-b border-[var(--border)] font-semibold">${
        dto.montoPagado || "-"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--muted-foreground)] border-b border-[var(--border)]">${
        fechaStr || "-"
      }</td>
      <td class="py-3 px-4 text-sm text-[var(--muted-foreground)] border-b border-[var(--border)]">${
        horaStr || "-"
      }</td>
    </tr>
  `;
      })
      .join("");
  } catch (error) {
    console.error("Error al renderizar tránsitos:", error);
  }
}

/**
 * Renderiza notificaciones desde el backend
 * Las notificaciones vienen del array propietario.notificaciones
 * con formato: { id, timestamp, mensaje, timestamp, mensajeCompleto }
 */
function renderizarNotificacionesDesdeBackend(notificaciones) {
  console.log("Renderizando notificaciones desde backend:", notificaciones);

  try {
    const container = document.getElementById("notificacionesContainer");
    if (!container) {
      console.error("No se encontró el contenedor de notificaciones");
      return;
    }

    if (!notificaciones || notificaciones.length === 0) {
      container.innerHTML = `
        <div class="py-4 px-4 text-sm text-[var(--muted-foreground)] text-center">
          No hay notificaciones
        </div>
      `;
      return;
    }

    // Las notificaciones ya vienen ordenadas por timestamp descendente desde el backend
    container.innerHTML = notificaciones
      .map((notif) => {
        // Determinar tipo de notificación por contenido del mensaje
        const mensaje = notif.mensaje || notif.mensajeCompleto || "";
        const esSaldoBajo =
          mensaje.toLowerCase().includes("saldo") &&
          mensaje.toLowerCase().includes("recarga");
        const esCambioEstado = mensaje
          .toLowerCase()
          .includes("estado en el sistema");

        // Iconos y colores según tipo
        const iconColor = esSaldoBajo
          ? "bg-[var(--destructive)]"
          : esCambioEstado
          ? "bg-[var(--chart-4)]"
          : "bg-[var(--primary)]";

        const iconPath = esSaldoBajo
          ? `<path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />`
          : `<path d="M10 2a6 6 0 00-6 6v3.586l-.707.707A1 1 0 004 14h12a1 1 0 00.707-1.707L16 11.586V8a6 6 0 00-6-6zM10 18a3 3 0 01-3-3h6a3 3 0 01-3 3z" />`;

        return `
          <div class="bg-[var(--background)] border border-[var(--border)] p-4 flex gap-4">
            <div class="flex-shrink-0">
              <div class="w-8 h-8 ${iconColor} border border-[var(--primary)] flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" viewBox="0 0 20 20" fill="currentColor">
                  ${iconPath}
                </svg>
              </div>
            </div>
            <div class="flex-1">
              <p class="text-sm text-[var(--foreground)]">${
                notif.mensaje || ""
              }</p>
              <p class="text-xs text-[var(--muted-foreground)] mt-2">${
                notif.timestamp || ""
              }</p>
            </div>
          </div>
        `;
      })
      .join("");
  } catch (error) {
    console.error("Error al renderizar notificaciones:", error);
  }
}
