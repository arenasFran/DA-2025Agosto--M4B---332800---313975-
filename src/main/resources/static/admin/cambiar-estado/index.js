// CONFIGURACIÓN SSE - Debe definirse ANTES de que vistaWeb.js haga su primer submit
urlIniciarVista = "/estado/vistaConectada";
urlCierreVista = "/estado/vistaCerrada";
urlRegistroSSE = "/estado/registrarSSE";

// State management
let propietarioActual = null;

// Event listeners
document
  .getElementById("btnBuscar")
  .addEventListener("click", buscarPropietario);
document
  .getElementById("btnCambiarEstado")
  .addEventListener("click", cambiarEstado);

// Handler for vistaWeb.js callback when propietario is found (restore or new search)
function mostrar_propietario(data) {
  console.log("Propietario encontrado:", data);
  propietarioActual = data;

  // Populate the cedula input field (important for page refresh/restore)
  document.getElementById("cedula").value = data.cedula;

  mostrarPropietarioInfo();
  ocultarMensaje();
}

// Handler for vistaWeb.js callback when estado is changed successfully
function mostrar_estadoCambiado(message) {
  mostrarMensaje(message, "success");
  // Refresh the propietario data to show updated estado
  buscarPropietario();
}

// Handler for vistaWeb.js callback when an error occurs
function mostrar_error(errorMessage) {
  mostrarMensaje(errorMessage, "error");
}

// Handler for SSE notifications
function mostrar_notificacion(mensaje) {
  console.log("📬 Notificación recibida:", mensaje);
  mostrarNotificacion("Notificación", mensaje, "info");
}

function buscarPropietario() {
  const cedula = document.getElementById("cedula").value.trim();

  if (!cedula) {
    mostrarMensaje("Por favor ingrese una cédula", "error");
    return;
  }

  // Use vistaWeb.js submit instead of fetch
  const params = "cedula=" + encodeURIComponent(cedula);
  submit("/estado/buscar", params, "POST");
}

function cambiarEstado() {
  if (!propietarioActual) {
    mostrarMensaje("Primero debe buscar un propietario", "error");
    return;
  }

  const nuevoEstado = document.getElementById("estado").value;

  if (!nuevoEstado) {
    mostrarMensaje("Debe seleccionar un estado", "error");
    return;
  }

  // Use vistaWeb.js submit instead of fetch
  const params =
    "cedula=" +
    encodeURIComponent(propietarioActual.cedula) +
    "&nuevoEstado=" +
    encodeURIComponent(nuevoEstado);
  submit("/estado/actualizar", params, "PUT");
}

function mostrarPropietarioInfo() {
  document.getElementById(
    "propietarioNombre"
  ).textContent = `${propietarioActual.nombre} (${propietarioActual.cedula})`;
  document.getElementById("propietarioEstado").textContent =
    propietarioActual.estado;

  // Enable estado dropdown and Cambiar Estado button
  const estadoSelect = document.getElementById("estado");
  estadoSelect.disabled = false;

  // Remove placeholder option if it exists
  const placeholderOption = estadoSelect.querySelector('option[value=""]');
  if (placeholderOption) {
    placeholderOption.remove();
  }

  // Preselect current estado
  estadoSelect.value = propietarioActual.estado;

  // Enable Cambiar Estado button
  document.getElementById("btnCambiarEstado").disabled = false;

  document.getElementById("propietarioInfo").style.display = "block";
}

function ocultarPropietarioInfo() {
  document.getElementById("propietarioInfo").style.display = "none";

  // Disable controls
  const estadoSelect = document.getElementById("estado");
  estadoSelect.disabled = true;

  // Add placeholder option back if it doesn't exist
  if (!estadoSelect.querySelector('option[value=""]')) {
    const placeholderOption = document.createElement("option");
    placeholderOption.value = "";
    placeholderOption.textContent = "Primero busque un propietario";
    estadoSelect.insertBefore(placeholderOption, estadoSelect.firstChild);
  }

  estadoSelect.value = ""; // Reset to placeholder
  document.getElementById("btnCambiarEstado").disabled = true;

  propietarioActual = null;
}

function mostrarMensaje(texto, tipo) {
  // Use toast component instead of inline messages
  const titulo =
    tipo === "error" ? "Error" : tipo === "success" ? "Éxito" : "Información";
  mostrarNotificacion(titulo, texto, tipo);
}

function ocultarMensaje() {
  // No longer needed - toast auto-closes
}

function cerrarSesion() {
  // Cerrar sesión usando vistaWeb.js
  submit("/login/logoutAdmin", "", "POST");
}
