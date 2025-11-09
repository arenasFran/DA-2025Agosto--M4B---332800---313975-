package obligatorio.obli.models;

import java.io.Serializable;
import java.util.Date;

import obligatorio.obli.models.Usuarios.Administrador;

public class AdminSesion implements Serializable {
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
