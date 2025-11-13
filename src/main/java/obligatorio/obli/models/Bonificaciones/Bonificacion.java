package obligatorio.obli.models.Bonificaciones;

import java.util.List;

import obligatorio.obli.models.Transito;

public abstract class Bonificacion {
    private String nombre;

    public Bonificacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract double calcularDescuento(Transito transito, List<Transito> transitosDelDia);

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Bonificacion bonificacion = (Bonificacion) obj;
        return this.nombre.equals(bonificacion.getNombre());
    }
}
