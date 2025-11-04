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
function mostrar_propietarioEncontrado(data) {
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
  const mensajeDiv = document.getElementById("mensaje");
  mensajeDiv.textContent = texto;
  if (tipo === "error") {
    mensajeDiv.className =
      "p-4 border border-[var(--destructive)] text-[var(--destructive)] bg-[var(--background)]";
  } else {
    mensajeDiv.className =
      "p-4 border border-[var(--chart-2)] text-[var(--chart-2)] bg-[var(--background)]";
  }
  mensajeDiv.style.display = "block";
}

function ocultarMensaje() {
  document.getElementById("mensaje").style.display = "none";
}

function cerrarSesion() {
  // Cerrar sesión usando vistaWeb.js
  submit("/login/logoutAdmin", "", "POST");
}
