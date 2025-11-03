// CONFIGURACIÓN SSE - Debe definirse ANTES de que vistaWeb.js haga su primer submit
urlIniciarVista = "/administrador/vistaConectada";
urlCierreVista = "/administrador/vistaCerrada";
urlRegistroSSE = "/administrador/registrarSSE";

// Cargar datos al iniciar la página
document.addEventListener("DOMContentLoaded", function () {
  cargarBonificaciones();
  cargarPuestos();
});

function cerrarSesion() {
  // Mostrar modal de confirmación
  const modal = document.getElementById("modalConfirm");
  modal.classList.remove("hidden");
  modal.classList.add("flex");
}

function cerrarModal() {
  const modal = document.getElementById("modalConfirm");
  modal.classList.add("hidden");
  modal.classList.remove("flex");
}

function confirmarCierreSesion() {
  cerrarModal();
  mostrarNotificacion("Cerrando sesión...", "Espere un momento", "info");

  // Intentar cerrar sesión de propietario primero
  fetch("/login/logoutPropietario", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
  })
    .then(async (response) => {
      if (response.ok) {
        const data = await response.json();
        // Si fue exitoso, redirigir
        if (data && data.length > 0) {
          procesarResultadosSubmit(data);
          return;
        }
      }
      // Si falló el logout de propietario, intentar admin
      return fetch("/login/logoutAdmin", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      });
    })
    .then(async (response) => {
      if (response && response.ok) {
        const data = await response.json();
        if (data && data.length > 0) {
          procesarResultadosSubmit(data);
        }
      } else {
        // Si ninguno funcionó, redirigir manualmente al login
        window.location.href = "login.html";
      }
    })
    .catch((err) => {
      console.error("Error al cerrar sesión:", err);
      mostrarNotificacion(
        "Error",
        "No se pudo cerrar sesión correctamente",
        "error"
      );
      // En caso de error, redirigir al login de todas formas después de 2 segundos
      setTimeout(() => {
        window.location.href = "login.html";
      }, 2000);
    });
}

function mostrarNotificacion(titulo, mensaje, tipo = "success") {
  const modal = document.getElementById("modalNotif");
  const content = document.getElementById("modalNotifContent");
  const icon = document.getElementById("modalNotifIcon");
  const title = document.getElementById("modalNotifTitle");
  const msg = document.getElementById("modalNotifMessage");

  // Configurar según el tipo
  if (tipo === "success") {
    content.className =
      "bg-[var(--card)] border shadow-xl p-4 max-w-sm border-l-4 border-l-[var(--chart-2)] transform transition-all";
    icon.innerHTML = `
      <div class="w-8 h-8 bg-[var(--muted)] border border-[var(--border)] flex items-center justify-center">
        <svg class="w-5 h-5 text-[var(--chart-2)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
        </svg>
      </div>
    `;
  } else if (tipo === "error") {
    content.className =
      "bg-[var(--card)] border shadow-xl p-4 max-w-sm border-l-4 border-l-[var(--destructive)] transform transition-all";
    icon.innerHTML = `
      <div class="w-8 h-8 bg-[var(--muted)] border border-[var(--border)] flex items-center justify-center">
        <svg class="w-5 h-5 text-[var(--destructive)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
        </svg>
      </div>
    `;
  } else if (tipo === "info") {
    content.className =
      "bg-[var(--card)] border shadow-xl p-4 max-w-sm border-l-4 border-l-[var(--primary)] transform transition-all";
    icon.innerHTML = `
      <div class="w-8 h-8 bg-[var(--muted)] border border-[var(--border)] flex items-center justify-center">
        <svg class="w-5 h-5 text-[var(--primary)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
        </svg>
      </div>
    `;
  }

  title.textContent = titulo;
  msg.textContent = mensaje;

  modal.classList.remove("hidden");

  // Auto cerrar después de 5 segundos (excepto para info que cierra más rápido)
  setTimeout(
    () => {
      cerrarNotificacion();
    },
    tipo === "info" ? 2000 : 5000
  );
}

function cerrarNotificacion() {
  const modal = document.getElementById("modalNotif");
  modal.classList.add("hidden");
}

function cargarBonificaciones() {
  fetch("/bonificaciones/get-bon")
    .then((res) => res.json())
    .then((bonificaciones) => {
      const select = document.getElementById("bonificacion");
      const lista = document.getElementById("listaBonificaciones");

      // Llenar el select
      select.innerHTML =
        '<option value="">Seleccione una bonificación</option>';
      bonificaciones.forEach((bon) => {
        const option = document.createElement("option");
        option.value = bon.nombre;
        option.textContent = bon.nombre;
        select.appendChild(option);
      });

      // Mostrar lista
      lista.innerHTML =
        bonificaciones.length > 0
          ? bonificaciones
              .map(
                (bon) => `
            <div class="p-3 bg-[var(--background)] border border-[var(--border)] border-l-4 border-l-[var(--chart-2)]">
              <span class="font-medium text-[var(--foreground)]">📌 ${bon.nombre}</span>
            </div>
          `
              )
              .join("")
          : '<p class="text-[var(--muted-foreground)] text-center py-4">No hay bonificaciones disponibles</p>';
    })
    .catch((err) => {
      console.error("Error al cargar bonificaciones:", err);
      document.getElementById("listaBonificaciones").innerHTML =
        '<p class="text-[var(--destructive)] text-center py-4">Error al cargar bonificaciones</p>';
    });
}

function cargarPuestos() {
  fetch("/bonificaciones/get-puestos")
    .then((res) => res.json())
    .then((puestos) => {
      const select = document.getElementById("puesto");
      const lista = document.getElementById("listaPuestos");

      // Llenar el select
      select.innerHTML = '<option value="">Seleccione un puesto</option>';
      puestos.forEach((puesto) => {
        const option = document.createElement("option");
        option.value = puesto.nombre;
        option.textContent = `${puesto.nombre} - ${puesto.direccion}`;
        select.appendChild(option);
      });

      // Mostrar lista
      lista.innerHTML =
        puestos.length > 0
          ? puestos
              .map(
                (puesto) => `
            <div class="p-4 bg-[var(--background)] border border-[var(--border)] border-l-4 border-l-[var(--chart-3)]">
              <h3 class="font-semibold text-[var(--foreground)] mb-1">${puesto.nombre}</h3>
              <p class="text-sm text-[var(--muted-foreground)]">📍 ${puesto.direccion}</p>
              ${
                puesto.tarifas && puesto.tarifas.length > 0
                  ? `<p class="text-xs text-[var(--muted-foreground)] mt-1">${puesto.tarifas.length} tarifa(s)</p>`
                  : ""
              }
            </div>
          `
              )
              .join("")
          : '<p class="text-[var(--muted-foreground)] text-center py-4 col-span-full">No hay puestos disponibles</p>';
    })
    .catch((err) => {
      console.error("Error al cargar puestos:", err);
      document.getElementById("listaPuestos").innerHTML =
        '<p class="text-[var(--destructive)] text-center py-4 col-span-full">Error al cargar puestos</p>';
    });
}

function buscarPropietario() {
  const ci = document.getElementById("ci").value.trim();
  const infoDiv = document.getElementById("propietarioInfo");

  if (!ci) {
    infoDiv.innerHTML =
      '<span class="text-[var(--destructive)]">⚠ Ingrese una cédula</span>';
    return;
  }

  // Mostrar mensaje de carga
  infoDiv.innerHTML = '<span class="text-[var(--muted-foreground)]">🔍 Buscando...</span>';

  fetch(`/bonificaciones/get-propietario?ci=${encodeURIComponent(ci)}`)
    .then(async (res) => {
      const status = res.status;
      const text = await res.text();

      if (!res.ok) {
        // Manejar error según vistaWeb.js
        if (status >= 400 && status < 500) {
          // Error del cliente (ej: 404 Not Found)
          try {
            const errorData = JSON.parse(text);
            throw new Error(errorData.message || "Propietario no encontrado");
          } catch (e) {
            if (e.message && e.message !== "Propietario no encontrado") {
              throw new Error(
                "No se encontró ningún propietario con la cédula ingresada"
              );
            }
            throw e;
          }
        } else {
          // Error del servidor
          throw new Error("Error al comunicarse con el servidor");
        }
      }
      return JSON.parse(text);
    })
    .then((propietario) => {
      // Cargar también las asignaciones
      return fetch(
        `/bonificaciones/get-asignaciones?ci=${encodeURIComponent(ci)}`
      )
        .then((res) => res.json())
        .then((asignaciones) => ({ propietario, asignaciones }))
        .catch((err) => ({ propietario, asignaciones: [] }));
    })
    .then(({ propietario, asignaciones }) => {
      // Construir lista de vehículos
      let vehiculosHtml = "";
      if (propietario.vehiculo && propietario.vehiculo.length > 0) {
        vehiculosHtml = propietario.vehiculo
          .map(
            (v) =>
              `<li class="text-xs">🚗 ${v.matricula} - ${
                v.modelo || "Vehículo"
              } (${v.color || "N/A"})</li>`
          )
          .join("");
      }

      // Construir lista de bonificaciones asignadas
      let bonificacionesHtml = "";
      if (asignaciones && asignaciones.length > 0) {
        bonificacionesHtml = asignaciones
          .map(
            (a) =>
              `<li class="text-xs bg-[var(--background)] p-2 border border-[var(--border)] border-l-4 border-l-[var(--chart-4)]">
            <strong>🎁 ${a.bonificacion.nombre}</strong><br>
            <span class="text-[var(--muted-foreground)]">📍 Puesto: ${a.puesto.nombre}</span>
          </li>`
          )
          .join("");
      }

      infoDiv.innerHTML = `
        <div class="bg-[var(--background)] border border-[var(--border)] border-l-4 border-l-[var(--chart-2)] p-3">
          <div class="flex items-start gap-2">
            <span class="text-[var(--chart-2)] text-lg">✓</span>
            <div class="flex-1">
              <p class="font-semibold text-[var(--foreground)]">
                ${propietario.nombre || "Sin nombre"}
              </p>
              <div class="text-xs text-[var(--foreground)] space-y-1 mt-1">
                <p><strong>ID:</strong> ${propietario.id}</p>
                <p><strong>CI:</strong> ${propietario.ci}</p>
                <p><strong>Estado:</strong> <span class="px-2 py-1 text-white ${getEstadoColor(
                  propietario.estado?.nombre
                )}">${propietario.estado?.nombre || "Sin estado"}</span></p>
                <p><strong>Saldo:</strong> $${
                  propietario.saldo ? propietario.saldo.toFixed(2) : "0.00"
                }</p>
                <p><strong>Saldo mínimo alerta:</strong> $${
                  propietario.saldoMinAlerta
                    ? propietario.saldoMinAlerta.toFixed(2)
                    : "0.00"
                }</p>
                ${
                  vehiculosHtml
                    ? `
                  <div class="mt-2">
                    <strong>Vehículos:</strong>
                    <ul class="ml-2 mt-1 space-y-1">${vehiculosHtml}</ul>
                  </div>
                `
                    : '<p class="text-[var(--muted-foreground)] italic">Sin vehículos registrados</p>'
                }
                ${
                  bonificacionesHtml
                    ? `
                  <div class="mt-2">
                    <strong>Bonificaciones asignadas:</strong>
                    <ul class="ml-2 mt-1 space-y-1">${bonificacionesHtml}</ul>
                  </div>
                `
                    : '<p class="text-[var(--muted-foreground)] italic mt-2">Sin bonificaciones asignadas</p>'
                }
              </div>
            </div>
          </div>
        </div>
      `;
    })
    .catch((err) => {
      infoDiv.innerHTML = `
        <div class="bg-[var(--background)] border border-[var(--border)] border-l-4 border-l-[var(--destructive)] p-3 flex items-start gap-2">
          <span class="text-[var(--destructive)] text-lg">✗</span>
          <p class="text-sm text-[var(--foreground)]">${err.message}</p>
        </div>
      `;
    });
}

function getEstadoColor(estado) {
  if (!estado) return "bg-[var(--muted)]";

  switch (estado.toLowerCase()) {
    case "habilitado":
      return "bg-[var(--chart-2)]";
    case "deshabilitado":
      return "bg-[var(--destructive)]";
    case "penalizado":
      return "bg-[var(--chart-5)]";
    case "suspendido":
      return "bg-[var(--chart-4)]";
    default:
      return "bg-[var(--muted)]";
  }
}

// Función para asignar bonificación usando vistaWeb.js
function asignarBonificacion(form) {
  // Serializar el formulario a formato URL-encoded
  const formData = new FormData(form);
  const urlEncodedData = new URLSearchParams(formData).toString();

  // Usar submit() de vistaWeb.js
  submit("/bonificaciones/asignar", urlEncodedData, "POST");
}

function mostrarMensaje(texto, tipo = "success") {
  mostrarNotificacion(
    tipo === "success" ? "Éxito" : tipo === "error" ? "Error" : "Información",
    texto,
    tipo
  );
}

// Función requerida por vistaWeb.js para manejar errores
function procesarErrorSubmit(status, text) {
  console.log(
    "Error capturado por vistaWeb.js - Status:",
    status,
    "Text:",
    text
  );

  // Intentar parsear el error como JSON
  try {
    const errorObj = JSON.parse(text);
    mostrarNotificacion("Error", errorObj.message || text, "error");
  } catch (e) {
    // Si no es JSON válido, mostrar el texto directamente
    mostrarNotificacion("Error", text || "Error en la operación", "error");
  }
}

// Handlers para vistaWeb.js
window["mostrar_asignacion exitosa"] = function (parametro) {
  mostrarMensaje(parametro || "Bonificación asignada correctamente", "success");

  // NO resetear el formulario, solo limpiar los selects de bonificación y puesto
  document.getElementById("bonificacion").value = "";
  document.getElementById("puesto").value = "";

  // Recargar la información del propietario para mostrar la nueva asignación
  const ci = document.getElementById("ci").value.trim();
  if (ci) {
    buscarPropietario();
  }
};

// Handler para página de login (si se redirige por sesión expirada)
window["mostrar_paginaLogin"] = function (parametro) {
  window.location.href = parametro || "login.html";
};

// Handler genérico para login exitoso
window["mostrar_login exitoso"] = function (parametro) {
  window.location.href = parametro || "bonificaciones.html";
};

// Handler para notificaciones en tiempo real via SSE
window["mostrar_notificacion"] = function (parametro) {
  mostrarNotificacion("Nueva bonificación asignada", parametro, "info");

  // Recargar la lista del propietario actual si hay uno buscado
  const ci = document.getElementById("ci").value.trim();
  if (ci) {
    buscarPropietario();
  }
};

// Handler para mensajes del servidor (nombre del admin)
window["mostrar_nombreAdmin"] = function (parametro) {
  console.log("Administrador conectado:", parametro);
};

// Handler genérico para mensajes
window["mostrar_mensaje"] = function (parametro) {
  console.log("Mensaje del servidor:", parametro);
};

// Manejo del cierre de conexión SSE (opcional)
function conexionSSECerrada(event) {
  console.log("Conexión SSE cerrada, intentando reconectar...");
  // La reconexión se maneja automáticamente en sse.js
}
