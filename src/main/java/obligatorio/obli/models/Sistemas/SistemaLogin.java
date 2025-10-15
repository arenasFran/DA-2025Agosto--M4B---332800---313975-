package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;

import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.models.Usuarios.User;

public class SistemaLogin {

    private static SistemaLogin instancia;

    private ArrayList<Propietario> ps = new ArrayList<>();
    private ArrayList<Administrador> as = new ArrayList<>();

    private SistemaLogin() {

    }

    public static SistemaLogin getInstancia() {
        if (instancia == null) {
            instancia = new SistemaLogin();
        }
        return instancia;
    }

    public Propietario loginPropietario(String ci, String password) {
        for (Propietario p : ps) {
            if (p.getCi().equals(ci) && p.getPassword().equals(password)) {
                return p;
            }
        }
        throw new RuntimeException("Usuario o contraseña incorrectos");
    }

    public Administrador loginAdmin(String ci, String password) {
        for (Administrador a : as) {
            if (a.getCi().equals(ci) && a.getPassword().equals(password)) {
                return a;
            }
        }
        throw new RuntimeException("Usuario o contraseña incorrectos");
    }

}
