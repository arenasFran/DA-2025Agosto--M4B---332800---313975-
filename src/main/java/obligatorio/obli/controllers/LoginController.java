package obligatorio.obli.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import obligatorio.obli.exceptions.login.LoginCredencialesInvalidasException;
import obligatorio.obli.exceptions.login.SistemaLoginException;
import obligatorio.obli.exceptions.propietario.estados.EstadoProhibidoIniciarSesionException;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/login")
public class LoginController {
    public static final String SESSION_PROPIETARIO_COOKIE = "propietario";
    public static final String SESSION_ADMIN_COOKIE = "administrador";

    private Fachada fachada;

    private LoginController() {
        fachada = Fachada.getInstancia();
    }

    @PostMapping("/propietario")
    public List<Respuesta> loginPropietario(HttpSession sessionHttp, @RequestParam String ci,
            @RequestParam String password)
            throws LoginCredencialesInvalidasException, EstadoProhibidoIniciarSesionException {
        Propietario propietario = this.fachada.loginPropietario(ci, password);

        if (!propietario.puedeIniciarSesion()) {
            throw new EstadoProhibidoIniciarSesionException(propietario.getEstado());
        }

        sessionHttp.setAttribute(SESSION_PROPIETARIO_COOKIE, propietario);

        return Respuesta.lista(new Respuesta("login exitoso", "propietario/dashboard.html"));
    }

    @PostMapping("/admin")
    public List<Respuesta> loginAdmin(HttpSession sessionHttp, @RequestParam String ci, @RequestParam String password)
            throws LoginCredencialesInvalidasException {
        Administrador admin = this.fachada.loginAdmin(ci, password);

        sessionHttp.setAttribute(SESSION_ADMIN_COOKIE, admin);

        return Respuesta.lista(new Respuesta("login exitoso", "admin/bonificaciones/index.html"));
    }

    @PostMapping("/logoutPropietario")
    public List<Respuesta> logoutPropietario(HttpSession sessionHttp) throws SistemaLoginException {
        sessionHttp.removeAttribute(SESSION_PROPIETARIO_COOKIE);
        return Respuesta.lista(new Respuesta("paginaLogin", "/login.html"));
    }

    @PostMapping("/logoutAdmin")
    public List<Respuesta> logoutAdmin(HttpSession sessionHttp) throws SistemaLoginException {
        sessionHttp.removeAttribute(SESSION_ADMIN_COOKIE);
        return Respuesta.lista(new Respuesta("paginaLogin", "/login.html"));
    }
}
