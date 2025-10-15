package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;

import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;

public class SistemaLogin {

    private static SistemaLogin instancia;
    private final SistemaUsuario sistemaUsuario;

    private SistemaLogin() {
        this.sistemaUsuario = SistemaUsuario.getInstancia();
    }

    public static SistemaLogin getInstancia() {
        if (instancia == null) {
            instancia = new SistemaLogin();
        }
        return instancia;
    }

    public Propietario loginPropietario(String ci, String password) {
        for (Propietario p : SistemaUsuario.getInstancia().getPropietarios()) {
            if (p.getCi().equals(ci) && p.getPassword().equals(password)) {
                return p;
            }
        }
        throw new RuntimeException("Usuario o contraseña incorrectos");
    }

    public Administrador loginAdmin(String ci, String password) {
        for (Administrador a : SistemaUsuario.getInstancia().getAdministradores()) {
            if (a.getCi().equals(ci) && a.getPassword().equals(password)) {
                return a;
            }
        }
        throw new RuntimeException("Usuario o contraseña incorrectos");
    }

}
