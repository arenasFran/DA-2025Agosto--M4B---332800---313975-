package obligatorio.obli.models;

import java.io.Serializable;
import java.util.Date;

import obligatorio.obli.models.Usuarios.Propietario;

public class PropietarioSesion implements Serializable {

    private Date fechaIngreso = new Date();

    private Propietario propietario;

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public PropietarioSesion(Propietario propietario) {

        this.propietario = propietario;
    }

}
