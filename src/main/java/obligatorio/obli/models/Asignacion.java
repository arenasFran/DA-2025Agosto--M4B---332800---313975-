package obligatorio.obli.models;

import obligatorio.obli.models.Bonificaciones.Bonificacion;

public class Asignacion {

    private Bonificacion bonificacion;
    private Puesto puesto;

    public Asignacion(Bonificacion bonificacion, Puesto puesto) {
        this.bonificacion = bonificacion;
        this.puesto = puesto;
        validar();
    }

    public Bonificacion getBonificacion() {
        return bonificacion;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    private void validar() {
        if (bonificacion == null) {
            throw new IllegalArgumentException("La bonificación no puede ser null");
        }
        if (puesto == null) {
            throw new IllegalArgumentException("El puesto no puede ser null");
        }
    }

    public boolean aplicaParaTransito(Transito transito) {
        if (transito == null || transito.getPuesto() == null) {
            return false;
        }
        return this.puesto.equals(transito.getPuesto());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Asignacion otra = (Asignacion) obj;
        return this.bonificacion.equals(otra.bonificacion) && this.puesto.equals(otra.puesto);
    }

    @Override
    public int hashCode() {
        return bonificacion.hashCode() + puesto.hashCode();
    }
}
