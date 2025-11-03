/* 
 * Librería para manejar Server-Sent Events (SSE)
 * Permite recibir notificaciones en tiempo real desde el servidor
 */

// Se asume que está incluida vistaWeb.js
// La página que incluya esta lib debe cargar esta variable 
// Para registrar SSE debe ejecutarse siempre antes el inicio de la vista 
var urlRegistroSSE = null;

// Esta función la llama vistaWeb.js al final del submit de inicio de la vista
function primerSubmitFinalizado(){
    registrarSSE();
}

function registrarSSE(){
    // Llamada al endpoint para recibir mensajes desde el servidor
    if (urlRegistroSSE !== null) {
        
        const eventSource = new EventSource(urlRegistroSSE, {withCredentials: true});
               
        // LLEGA UN MENSAJE DESDE EL SERVIDOR!
        eventSource.onmessage = function (event) {
            // Se asume que todos los mensajes llegan en formato JSON
            json = JSON.parse(event.data); // Convertir el JSON a objeto
            procesarMensajeSSE(json); 
        };
        
        // ERROR EN LA CONEXIÓN CON EL SERVIDOR
        eventSource.onerror = function (event) {
            // En todos los casos se cierra el event source
            eventSource.close();
            
            try {
                // Método que puede estar definido en la página que incluya esta lib 
                // para personalizar el manejo del error en la conexión SSE   
                conexionSSECerrada(event);
            } catch (e) {
                // Por defecto se "borra" la página
                console.error('Conexión SSE cerrada:', e);
                // Opcionalmente, intentar reconectar después de un tiempo
                setTimeout(() => {
                    console.log('Intentando reconectar SSE...');
                    registrarSSE();
                }, 5000); // Reintentar después de 5 segundos
            }
        };
    }
}

// Por defecto se asume que los mensajes recibidos vía SSE tienen el mismo formato que las respuestas
// del submit. 
function procesarMensajeSSE(mensaje){
    procesarResultadosSubmit(mensaje);
}
