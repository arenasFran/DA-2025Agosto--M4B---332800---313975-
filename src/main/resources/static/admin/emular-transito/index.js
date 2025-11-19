// CONFIGURACIÓN SSE - Debe definirse ANTES de que vistaWeb.js haga su primer submit
urlIniciarVista = "/transitos/vistaConectada";
urlCierreVista = "/transitos/vistaCerrada";
urlRegistroSSE = "/transitos/registrarSSE";

// State management
let puestosDisponibles = [];

window["mostrar_puestos"] = function (puestos) {
  console.log("Puestos recibidos desde vistaWeb.js:", puestos);

  puestosDisponibles = puestos || [];

  const select = document.getElementById("puesto");
  select.innerHTML = '<option value="">Seleccione un puesto</option>';

  puestos.forEach((puesto) => {
    const option = document.createElement("option");
    option.value = puesto.nombre;
    option.textContent = puesto.nombre;
    select.appendChild(option);
  });
};

/**
 * Handler: Mostrar tarifas del puesto seleccionado
 * Llamado por vistaWeb.js cuando se ejecuta submit("/transitos/tarifas?nombrePuesto=X", ...)
 */
window["mostrar_tarifas"] = function (tarifas) {
  console.log("Tarifas recibidas desde vistaWeb.js:", tarifas);

  const listaTarifas = document.getElementById("listaTarifas");

  if (!tarifas || tarifas.length === 0) {
    listaTarifas.innerHTML =
      '<p class="text-[var(--muted-foreground)] text-center py-4">Sin tarifas disponibles</p>';
    return;
  }

  listaTarifas.innerHTML = tarifas
    .map(
      (tarifa) => `
      <div class="p-3 bg-[var(--background)] border border-[var(--border)] border-l-4 border-l-[var(--chart-3)]">
        <div class="flex justify-between items-center">
          <span class="font-semibold text-[var(--foreground)]">
            📋 ${tarifa.nombreCategoria}
          </span>
          <span class="text-[var(--chart-3)] font-bold">
            $${tarifa.monto ? tarifa.monto.toFixed(2) : "0.00"}
          </span>
        </div>
      </div>
    `
    )
    .join("");
};

/**
 * Handler: Tránsito emulado exitosamente
 * Llamado por vistaWeb.js cuando emularTransito() retorna éxito
 */
window["mostrar_transito_emulado"] = function (detalleTransito) {
  console.log("Tránsito emulado exitosamente:", detalleTransito);

  // Mostrar notificación de éxito
  mostrarNotificacion("Éxito", "Tránsito registrado exitosamente", "success");

  // Mostrar resultado en componente visual
  mostrarResultado(detalleTransito);

  // Limpiar formulario
  document.getElementById("formTransito").reset();
  document.getElementById("listaTarifas").innerHTML =
    '<p class="text-[var(--muted-foreground)] text-center py-4">Seleccione un puesto para ver sus tarifas</p>';
};

/**
 * Handler: Error al emular tránsito
 */
window["mostrar_error_transito"] = function (parametro) {
  console.log("Error al emular tránsito:", parametro);
  mostrarNotificacion(
    "❌ Error",
    parametro || "No se pudo emular el tránsito",
    "error"
  );
};

/**
 * Handler: Notificación del administrador
 */
window["mostrar_nombreAdmin"] = function (parametro) {
  console.log("Administrador conectado:", parametro);
};

/**
 * Handler: Mensaje genérico
 */
window["mostrar_mensaje"] = function (parametro) {
  console.log("Mensaje del servidor:", parametro);
};

/**
 * Handler: Redirigir a login (sesión expirada)
 */
window["mostrar_paginaLogin"] = function (parametro) {
  window.location.href = parametro || "/login.html";
};

// ============================================================================
// FUNCIONES DE INTERFAZ
// ============================================================================

/**
 * Cargar puestos disponibles
 */
function cargarPuestos() {
  console.log("Cargando puestos...");
  submit("/transitos/puestos", "", "GET");
}

/**
 * Cargar tarifas cuando el usuario selecciona un puesto
 */
function cargarTarifas() {
  const nombrePuesto = document.getElementById("puesto").value;

  if (!nombrePuesto) {
    document.getElementById("listaTarifas").innerHTML =
      '<p class="text-[var(--muted-foreground)] text-center py-4">Seleccione un puesto para ver sus tarifas</p>';
    return;
  }

  console.log("Cargando tarifas para puesto:", nombrePuesto);
  const params = "nombrePuesto=" + encodeURIComponent(nombrePuesto);
  submit("/transitos/tarifas", params, "GET");
}

/**
 * Emular un tránsito
 */
function emularTransito(form) {
  const nombrePuesto = document.getElementById("puesto").value;
  const matricula = document.getElementById("matricula").value.trim();
  const fechaHora = document.getElementById("fechaHora").value;

  // Validaciones
  if (!nombrePuesto) {
    mostrarNotificacion("⚠️ Validación", "Seleccione un puesto", "error");
    return;
  }

  if (!matricula) {
    mostrarNotificacion(
      "⚠️ Validación",
      "Ingrese la matrícula del vehículo",
      "error"
    );
    return;
  }

  if (!fechaHora) {
    mostrarNotificacion("⚠️ Validación", "Seleccione fecha y hora", "error");
    return;
  }

  console.log("Emulando tránsito:", { nombrePuesto, matricula, fechaHora });

  // Convertir datetime-local a formato esperado por backend (dd/MM/yyyy HH:mm:ss)
  const fecha = new Date(fechaHora);
  const dia = String(fecha.getDate()).padStart(2, "0");
  const mes = String(fecha.getMonth() + 1).padStart(2, "0");
  const año = fecha.getFullYear();
  const horas = String(fecha.getHours()).padStart(2, "0");
  const minutos = String(fecha.getMinutes()).padStart(2, "0");
  const segundos = "00"; // Siempre 00 para datetime-local
  const fechaFormato = `${dia}/${mes}/${año} ${horas}:${minutos}:${segundos}`;

  // Construir params para vistaWeb.js
  const params = new URLSearchParams({
    matricula: matricula,
    nombrePuesto: nombrePuesto,
    fechaHora: fechaFormato,
  }).toString();

  // Usar vistaWeb.js - esto llamará window["mostrar_transito_emulado"]() cuando responda
  submit("/transitos/emular", params, "POST");
}

// ============================================================================
// FUNCIONES DE SESIÓN
// ============================================================================

function cerrarSesion() {
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

  fetch("/login/logoutPropietario", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
  })
    .then(async (response) => {
      if (response.ok) {
        const data = await response.json();
        if (data && data.length > 0) {
          procesarResultadosSubmit(data);
          return;
        }
      }

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
        window.location.href = "/login.html";
      }
    })
    .catch((err) => {
      console.error("Error al cerrar sesión:", err);
      mostrarNotificacion(
        "Error",
        "No se pudo cerrar sesión correctamente",
        "error"
      );
      setTimeout(() => {
        window.location.href = "/login.html";
      }, 2000);
    });
}

// ============================================================================
// FUNCIONES DE NOTIFICACIÓN
// ============================================================================
// Toast notifications are now handled by components/toast.js

/**
 * Mostrar resultado del tránsito emulado
 */
function mostrarResultado(detalleTransito) {
  const resultDisplay = document.getElementById("resultDisplay");

  if (!resultDisplay) {
    console.error("No se encontró el elemento resultDisplay");
    return;
  }

  // Populate fields
  document.getElementById("resultPuesto").textContent =
    detalleTransito.puesto || "-";
  document.getElementById("resultMatricula").textContent =
    detalleTransito.matricula || "-";
  document.getElementById("resultCategoria").textContent =
    detalleTransito.categoria || "-";
  document.getElementById("resultFecha").textContent =
    detalleTransito.fecha || "-";
  document.getElementById("resultMontoTarifa").textContent =
    detalleTransito.montoTarifa || "-";
  document.getElementById("resultBonificacion").textContent =
    detalleTransito.bonificacion || "-";
  document.getElementById("resultMontoBonificacion").textContent =
    detalleTransito.montoBonificacion || "-";
  document.getElementById("resultMontoPagado").textContent =
    detalleTransito.montoPagado || "-";

  // Show the result component
  resultDisplay.classList.remove("hidden");

  // Scroll to result
  resultDisplay.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

/**
 * Cerrar resultado del tránsito
 */
function cerrarResultado() {
  const resultDisplay = document.getElementById("resultDisplay");
  if (resultDisplay) {
    resultDisplay.classList.add("hidden");
  }
}

/**
 * Función requerida por vistaWeb.js para manejar errores
 */
function procesarErrorSubmit(status, text) {
  console.log(
    "Error capturado por vistaWeb.js - Status:",
    status,
    "Text:",
    text
  );

  try {
    const errorObj = JSON.parse(text);
    mostrarNotificacion("Error", errorObj.message || text, "error");
  } catch (e) {
    mostrarNotificacion("Error", text || "Error en la operación", "error");
  }
}

// ============================================================================
// INICIALIZACIÓN
// ============================================================================

document.addEventListener("DOMContentLoaded", function () {
  // Initialize toast component
  importToast();

  // Establecer fecha/hora actual como valor por defecto
  const ahora = new Date();
  ahora.setMinutes(ahora.getMinutes() - ahora.getTimezoneOffset());
  document.getElementById("fechaHora").value = ahora.toISOString().slice(0, 16);

  // Cargar puestos al iniciar
  cargarPuestos();
});
