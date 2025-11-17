// CONFIGURACIÓN SSE - Debe definirse ANTES de que vistaWeb.js haga su primer submit
urlIniciarVista = "/transitos/vistaConectada";
urlCierreVista = "/transitos/vistaCerrada";
urlRegistroSSE = "/administrador/registrarSSE";

// State management
let puestosDisponibles = [];
let historicoTrasitos = [];


window["mostrar_puestos"] = function(puestos) {
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
window["mostrar_tarifas"] = function(tarifas) {
  console.log("Tarifas recibidas desde vistaWeb.js:", tarifas);
  
  const listaTarifas = document.getElementById("listaTarifas");
  
  if (!tarifas || tarifas.length === 0) {
    listaTarifas.innerHTML = '<p class="text-[var(--muted-foreground)] text-center py-4">Sin tarifas disponibles</p>';
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
            $${tarifa.monto ? tarifa.monto.toFixed(2) : '0.00'}
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
window["mostrar_transito_emulado"] = function(detalleTransito) {
  console.log("Tránsito emulado exitosamente:", detalleTransito);
  
  // Mostrar notificación de éxito
  mostrarNotificacion("✅ Éxito", 
    `Tránsito registrado: $${detalleTransito.montoDescontado.toFixed(2)} descontado`, 
    "success");
  
  // Agregar a historial
  if (!historicoTrasitos) {
    historicoTrasitos = [];
  }
  historicoTrasitos.unshift(detalleTransito); // Agregar al inicio
  
  // Actualizar vista de historial
  actualizarHistorial();
  
  // Limpiar formulario
  document.getElementById("formTransito").reset();
  document.getElementById("listaTarifas").innerHTML = 
    '<p class="text-[var(--muted-foreground)] text-center py-4">Seleccione un puesto para ver sus tarifas</p>';
};

/**
 * Handler: Error al emular tránsito
 */
window["mostrar_error_transito"] = function(parametro) {
  console.log("Error al emular tránsito:", parametro);
  mostrarNotificacion("❌ Error", parametro || "No se pudo emular el tránsito", "error");
};

/**
 * Handler: Restaurar historial de tránsitos desde el servidor
 * Llamado por vistaWeb.js cuando vistaConectada() retorna el historial
 */
window["mostrar_historial_transitos"] = function(transitos) {
  console.log("Restaurando historial desde servidor:", transitos);
  
  if (transitos && transitos.length > 0) {
    historicoTrasitos = transitos;
    actualizarHistorial();
  }
};

/**
 * Handler: Notificación del administrador
 */
window["mostrar_nombreAdmin"] = function(parametro) {
  console.log("Administrador conectado:", parametro);
};

/**
 * Handler: Mensaje genérico
 */
window["mostrar_mensaje"] = function(parametro) {
  console.log("Mensaje del servidor:", parametro);
};

/**
 * Handler: Redirigir a login (sesión expirada)
 */
window["mostrar_paginaLogin"] = function(parametro) {
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
    mostrarNotificacion("⚠️ Validación", "Ingrese la matrícula del vehículo", "error");
    return;
  }
  
  if (!fechaHora) {
    mostrarNotificacion("⚠️ Validación", "Seleccione fecha y hora", "error");
    return;
  }
  
  console.log("Emulando tránsito:", { nombrePuesto, matricula, fechaHora });
  
  // Convertir datetime-local a formato ISO esperado (yyyy-MM-ddTHH:mm)
  const fecha = new Date(fechaHora);
  const fechaFormato = fecha.toISOString().slice(0, 16); // yyyy-MM-ddTHH:mm
  
  // Construir params para vistaWeb.js
  const params = new URLSearchParams({
    matricula: matricula,
    nombrePuesto: nombrePuesto,
    fechaHora: fechaFormato
  }).toString();
  
  // Usar vistaWeb.js - esto llamará window["mostrar_transito_emulado"]() cuando responda
  submit("/transitos/emular", params, "POST");
}

/**
 * Actualizar vista del historial de tránsitos
 */
function actualizarHistorial() {
  const listaHistorial = document.getElementById("listaHistorial");
  
  if (!historicoTrasitos || historicoTrasitos.length === 0) {
    listaHistorial.innerHTML = `
      <div class="p-4 bg-[var(--background)] border border-[var(--border)] border-l-4 border-l-[var(--muted)]">
        <p class="text-[var(--muted-foreground)] text-center">
          No hay tránsitos registrados
        </p>
      </div>
    `;
    return;
  }
  
  listaHistorial.innerHTML = historicoTrasitos
    .map((transito, index) => `
      <div class="p-4 bg-[var(--background)] border border-[var(--border)] border-l-4 border-l-[var(--chart-3)]">
        <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">
          <!-- Propietario -->
          <div>
            <p class="text-xs font-semibold text-[var(--muted-foreground)] uppercase">Propietario</p>
            <p class="text-sm text-[var(--foreground)]">${transito.propietarioNombre}</p>
            <p class="text-xs text-[var(--muted-foreground)]">CI: ${transito.propietarioCi}</p>
            <p class="text-xs font-semibold text-[var(--chart-1)]">Estado: ${transito.propietarioEstado}</p>
          </div>
          
          <!-- Vehículo -->
          <div>
            <p class="text-xs font-semibold text-[var(--muted-foreground)] uppercase">Vehículo</p>
            <p class="text-sm text-[var(--foreground)] font-mono">${transito.vehiculoMatricula}</p>
            <p class="text-xs text-[var(--muted-foreground)]">${transito.vehiculoModelo}</p>
            <p class="text-xs text-[var(--muted-foreground)]">Cat: ${transito.categoriaVehiculo}</p>
          </div>
          
          <!-- Puesto -->
          <div>
            <p class="text-xs font-semibold text-[var(--muted-foreground)] uppercase">Puesto</p>
            <p class="text-sm text-[var(--foreground)]">${transito.puestoNombre}</p>
          </div>
          
          <!-- Bonificación y Monto -->
          <div>
            <p class="text-xs font-semibold text-[var(--muted-foreground)] uppercase">Bonificación</p>
            <p class="text-sm text-[var(--foreground)]">${transito.bonificacion}</p>
            <p class="text-xs font-bold text-[var(--chart-3)]">-$${transito.montoDescontado.toFixed(2)}</p>
          </div>
          
          <!-- Saldo -->
          <div>
            <p class="text-xs font-semibold text-[var(--muted-foreground)] uppercase">Saldo</p>
            <p class="text-xs text-[var(--muted-foreground)]">Antes: $${transito.saldoAntes.toFixed(2)}</p>
            <p class="text-sm font-bold text-[var(--chart-2)]">Después: $${transito.saldoDespues.toFixed(2)}</p>
          </div>
        </div>
      </div>
    `)
    .join("");
}

/**
 * Cargar historial de tránsitos
 */
function cargarHistorial() {
  actualizarHistorial();
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
      mostrarNotificacion("Error", "No se pudo cerrar sesión correctamente", "error");
      setTimeout(() => {
        window.location.href = "/login.html";
      }, 2000);
    });
}

// ============================================================================
// FUNCIONES DE NOTIFICACIÓN
// ============================================================================

function mostrarNotificacion(titulo, mensaje, tipo = "success") {
  const modal = document.getElementById("modalNotif");
  const content = document.getElementById("modalNotifContent");
  const icon = document.getElementById("modalNotifIcon");
  const title = document.getElementById("modalNotifTitle");
  const msg = document.getElementById("modalNotifMessage");

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

/**
 * Función requerida por vistaWeb.js para manejar errores
 */
function procesarErrorSubmit(status, text) {
  console.log("Error capturado por vistaWeb.js - Status:", status, "Text:", text);

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
  // Establecer fecha/hora actual como valor por defecto
  const ahora = new Date();
  ahora.setMinutes(ahora.getMinutes() - ahora.getTimezoneOffset());
  document.getElementById("fechaHora").value = ahora.toISOString().slice(0, 16);
  
  // Inicializar historial vacío
  historicoTrasitos = [];
  actualizarHistorial();
  
  // Cargar puestos al iniciar
  cargarPuestos();
});
