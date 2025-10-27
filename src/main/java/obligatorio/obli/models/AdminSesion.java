package obligatorio.obli.models;

import java.sql.Date;

import obligatorio.obli.models.Usuarios.Administrador;

public class AdminSesion {

    public Date fechaIngreso;

    public Administrador admnin;

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public Administrador getAdmnin() {
        return admnin;
    }

    public AdminSesion(Date fechaIngreso, Administrador admnin) {
        this.fechaIngreso = fechaIngreso;
        this.admnin = admnin;
    }

}
