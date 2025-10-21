package obligatorio.obli.models;

import obligatorio.obli.models.Usuarios.Propietario;

public class Asignacion {

    public Propietario propietario;
    public Bonificacion bonificacion;
    public Puesto puesto;

    public Asignacion(Propietario propietario, Bonificacion bonificacion, Puesto puesto) {
        this.propietario = propietario;
        this.bonificacion = bonificacion;
        this.puesto = puesto;
    }

}
