package obligatorio.obli.models;

public class Asignacion {

    public Bonificacion bonificacion;
    public Puesto puesto;

    public Asignacion(Bonificacion bonificacion, Puesto puesto) {
        this.bonificacion = bonificacion;
        this.puesto = puesto;
    }

}
