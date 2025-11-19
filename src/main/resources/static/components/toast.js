/**
 * Reusable Toast Notification Component
 *
 * Usage:
 *   importToast(); // Call once to initialize toast HTML structure
 *   mostrarNotificacion("Título", "Mensaje", "success"); // success, error, info, warning
 *   cerrarNotificacion(); // Manually close toast
 */

/**
 * Initializes the toast HTML structure if it doesn't exist
 * Call this once when the page loads
 */
function importToast() {
  // Check if toast already exists
  if (document.getElementById("modalNotif")) {
    return;
  }

  // Create toast container
  const toastContainer = document.createElement("div");
  toastContainer.id = "modalNotif";
  toastContainer.className = "fixed top-4 right-4 z-50 hidden";
  toastContainer.innerHTML = `
    <div
      class="bg-[var(--card)] border shadow-xl p-4 max-w-sm border-l-4 transform transition-all"
      id="modalNotifContent"
    >
      <div class="flex items-start gap-3">
        <div id="modalNotifIcon" class="flex-shrink-0"></div>
        <div class="flex-1">
          <h4 id="modalNotifTitle" class="font-semibold text-[var(--foreground)]"></h4>
          <p id="modalNotifMessage" class="text-sm text-[var(--muted-foreground)] mt-1"></p>
        </div>
        <button
          onclick="cerrarNotificacion()"
          class="text-[var(--muted-foreground)] hover:text-[var(--foreground)]"
        >
          <svg
            class="w-5 h-5"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            ></path>
          </svg>
        </button>
      </div>
    </div>
  `;

  // Append to body
  document.body.appendChild(toastContainer);
}

/**
 * Shows a toast notification
 * @param {string} titulo - Title of the notification
 * @param {string} mensaje - Message content
 * @param {string} tipo - Type: "success", "error", "info", "warning" (default: "success")
 */
function mostrarNotificacion(titulo, mensaje, tipo = "success") {
  // Ensure toast HTML is initialized
  importToast();

  const modal = document.getElementById("modalNotif");
  const content = document.getElementById("modalNotifContent");
  const icon = document.getElementById("modalNotifIcon");
  const title = document.getElementById("modalNotifTitle");
  const msg = document.getElementById("modalNotifMessage");

  if (!modal || !content || !icon || !title || !msg) {
    console.error("Toast elements not found");
    return;
  }

  // Set styles and icon based on type
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
  } else if (tipo === "warning") {
    content.className =
      "bg-[var(--card)] border shadow-xl p-4 max-w-sm border-l-4 border-l-[var(--chart-4)] transform transition-all";
    icon.innerHTML = `
      <div class="w-8 h-8 bg-[var(--muted)] border border-[var(--border)] flex items-center justify-center">
        <svg class="w-5 h-5 text-[var(--chart-4)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
        </svg>
      </div>
    `;
  }

  title.textContent = titulo;
  msg.textContent = mensaje;

  modal.classList.remove("hidden");

  // Auto close after timeout (info closes faster)
  const timeout = tipo === "info" ? 2000 : 5000;
  setTimeout(() => {
    cerrarNotificacion();
  }, timeout);
}

/**
 * Closes the toast notification
 */
function cerrarNotificacion() {
  const modal = document.getElementById("modalNotif");
  if (modal) {
    modal.classList.add("hidden");
  }
}

/**
 * Convenience function to show success message
 */
function mostrarMensaje(texto, tipo = "success") {
  const titulo =
    tipo === "success"
      ? "Éxito"
      : tipo === "error"
      ? "Error"
      : tipo === "info"
      ? "Información"
      : "Advertencia";
  mostrarNotificacion(titulo, texto, tipo);
}
