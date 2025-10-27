package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import org.springframework.boot.actuate.web.exchanges.HttpExchange.Session;

import obligatorio.obli.exceptions.SistemaLoginException;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.PropietarioSesion;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;

public class SistemaLogin {

    private static SistemaLogin instancia;

    private ArrayList<PropietarioSesion> sesionesPropietarios = new ArrayList<>();
    private ArrayList<AdminSesion> sesionesAdmins = new ArrayList<>();

    private SistemaLogin() {
        SistemaUsuario.getInstancia();
    }

    public static SistemaLogin getInstancia() {
        if (instancia == null) {
            instancia = new SistemaLogin();
        }
        return instancia;
    }

    public PropietarioSesion loginPropietario(String ci, String password) throws SistemaLoginException {
        PropietarioSesion s = null;

        Propietario p = (Propietario) SistemaUsuario.getInstancia().devolverPorpietarioPorCi(ci);
        if (p != null) {
            s = new PropietarioSesion(p);
            sesionesPropietarios.add(s);
            return s;

        }
        throw new SistemaLoginException("Login incorrecto");

    }

    public AdminSesion loginAdmin(String ci, String password) throws SistemaLoginException {
        AdminSesion s = null;

        Administrador a = SistemaUsuario.getInstancia().devolverAdministradorPorCi(ci);

        if (a != null) {
            s = new AdminSesion(a);
            sesionesAdmins.add(s);
            return s;
        }
        throw new SistemaLoginException("Usuario o contraseña incorrectos");
    }

    public void logoutPropietario(PropietarioSesion sesion) {
        sesionesPropietarios.remove(sesion);
    }

    public void logoutAdmin(AdminSesion sesion) {
        sesionesAdmins.remove(sesion);
    }

}
