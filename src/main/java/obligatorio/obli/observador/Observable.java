package obligatorio.obli.observador;

import java.util.ArrayList;

/**
 * Clase base Observable del patrón Observer
 * Mantiene una lista de observadores y los notifica cuando ocurre un evento
 */
public class Observable {

    private ArrayList<Observador> observadores = new ArrayList<>();

    /**
     * Agrega un observador a la lista (si no está ya presente)
     */
    public void agregarObservador(Observador obs) {
        if (!observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    /**
     * Quita un observador de la lista
     */
    public void quitarObservador(Observador obs) {
        observadores.remove(obs);
    }

    /**
     * Notifica a todos los observadores que ocurrió un evento
     * 
     * @param evento El tipo de evento que ocurrió
     */
    public void avisar(Object evento) {
        // Crear una copia para evitar ConcurrentModificationException
        ArrayList<Observador> copia = new ArrayList<>(observadores);
        for (Observador obs : copia) {
            obs.actualizar(evento, this);
        }
    }
}
