package obligatorio.obli.models;

import java.sql.Date;

import obligatorio.obli.models.Usuarios.Administrador;

public class PropietarioSesion {

    public Date fechaIngreso;

    public Administrador propietario;

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public Administrador getPropietario() {
        return propietario;
    }

    public PropietarioSesion(Date fechaIngreso, Administrador propietario) {
        this.fechaIngreso = fechaIngreso;
        this.propietario = propietario;
    }

}
