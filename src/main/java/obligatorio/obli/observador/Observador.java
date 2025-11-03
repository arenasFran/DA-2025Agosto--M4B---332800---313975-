package obligatorio.obli.observador;

/**
 * Interfaz Observador del patrón Observer
 * Las clases que implementen esta interfaz recibirán notificaciones cuando ocurra un evento
 */
public interface Observador {
    
    /**
     * Método llamado cuando el Observable notifica un cambio
     * @param evento El tipo de evento que ocurrió
     * @param origen El Observable que generó la notificación
     */
    public void actualizar(Object evento, Observable origen);
}
