package obligatorio.obli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Componente que maneja la conexión SSE (Server-Sent Events) con cada navegador
 * Scope: session - Una instancia por sesión HTTP
 */
@Component
@Scope("session")
public class ConexionNavegador {

    private SseEmitter conexionSSE;

    /**
     * Establece una nueva conexión SSE
     * Si ya existe una conexión, la cierra antes de crear una nueva
     */
    public void conectarSSE() {
        if (conexionSSE != null) {
            cerrarConexion();
        }
        long timeOut = 30 * 60 * 1000; // 30 minutos de timeout (igual al valor por defecto de la sesión)
        conexionSSE = new SseEmitter(timeOut);
    }

    /**
     * Cierra la conexión SSE actual
     */
    public void cerrarConexion() {
        try {
            if (conexionSSE != null) {
                conexionSSE.complete();
                conexionSSE = null;
            }
        } catch (Exception e) {
            // Error silencioso al cerrar conexión
        }
    }

    /**
     * Obtiene el SseEmitter actual (para retornarlo en el endpoint)
     */
    public SseEmitter getConexionSSE() {
        return conexionSSE;
    }

    /**
     * Envía un objeto como JSON a través de SSE
     * 
     * @param informacion El objeto a enviar (se convierte automáticamente a JSON)
     */
    public void enviarJSON(Object informacion) {
        try {
            String json = new ObjectMapper().writeValueAsString(informacion);
            enviarMensaje(json);
        } catch (JsonProcessingException e) {
            // Error silencioso al serializar
        }
    }

    /**
     * Envía un mensaje de texto a través de SSE
     * 
     * @param mensaje El mensaje a enviar
     */
    public void enviarMensaje(String mensaje) {
        if (conexionSSE == null)
            return;

        try {
            conexionSSE.send(mensaje);
        } catch (Throwable e) {
            cerrarConexion();
        }
    }
}
