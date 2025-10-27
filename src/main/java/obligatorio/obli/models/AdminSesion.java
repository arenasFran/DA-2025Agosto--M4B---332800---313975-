package obligatorio.obli.models;

import java.util.Date;

import obligatorio.obli.models.Usuarios.Administrador;

public class AdminSesion {

    private Date fechaIngreso = new Date();

    private Administrador admnin;

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public Administrador getAdmnin() {
        return admnin;
    }

    public AdminSesion(Administrador admnin) {
        this.admnin = admnin;
    }

}
