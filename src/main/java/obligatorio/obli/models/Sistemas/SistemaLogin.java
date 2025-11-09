package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;

import obligatorio.obli.exceptions.administrador.AdministradorNoEncontradoException;
import obligatorio.obli.exceptions.login.LoginCredencialesInvalidasException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;
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

    public PropietarioSesion loginPropietario(String ci, String password)
            throws LoginCredencialesInvalidasException {
        try {
            Propietario prop = SistemaUsuario.getInstancia().getPropietarioPorCi(ci);

            PropietarioSesion s = new PropietarioSesion(prop);
            sesionesPropietarios.add(s);
            return s;
        } catch (PropietarioNoEncontradoException e) {
            throw new LoginCredencialesInvalidasException();
        }
    }

    public AdminSesion loginAdmin(String ci, String password) throws LoginCredencialesInvalidasException {
        try {
            Administrador admin = SistemaUsuario.getInstancia().getAdministradorPorCi(ci);

            AdminSesion s = new AdminSesion(admin);
            sesionesAdmins.add(s);
            return s;
        } catch (AdministradorNoEncontradoException e) {
            throw new LoginCredencialesInvalidasException();
        }
    }

    public void logoutPropietario(PropietarioSesion sesion) {
        sesionesPropietarios.remove(sesion);
    }

    public void logoutAdmin(AdminSesion sesion) {
        sesionesAdmins.remove(sesion);
    }

}
