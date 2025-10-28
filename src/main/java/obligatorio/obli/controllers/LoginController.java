package obligatorio.obli.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import obligatorio.obli.exceptions.SistemaLoginException;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.PropietarioSesion;
import obligatorio.obli.models.Sistemas.Fachada;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping("/propietario")
    public List<Respuesta> loginPropietario(HttpSession sessionHttp, @RequestParam String ci,
            @RequestParam String password) throws SistemaLoginException {
        PropietarioSesion sesion = Fachada.getInstancia().loginPropietario(ci, password);

        sessionHttp.setAttribute("propietario", sesion);
        return Respuesta.lista(new Respuesta("login exitoso", "menu.html"));

    }

    @PostMapping("/admin")
    public List<Respuesta> loginAdmin(HttpSession sessionHttp, @RequestParam String ci, @RequestParam String password)
            throws SistemaLoginException {
        AdminSesion a = Fachada.getInstancia().loginAdmin(ci, password);
        sessionHttp.setAttribute("admin", a);

        return Respuesta.lista(new Respuesta("login exitoso", "bonificaciones.html"));
    }

    @PostMapping("/logoutPropietario")
    public List<Respuesta> logoutPropietario(HttpSession sessionHttp) throws SistemaLoginException {
        PropietarioSesion sesion = (PropietarioSesion) sessionHttp.getAttribute("propietario");
        if (sesion != null) {
            Fachada.getInstancia().logoutPropietario(sesion);
            sessionHttp.removeAttribute("propietario");
        }
        return Respuesta.lista(new Respuesta("paginaLogin", "login.html"));
    }

    @PostMapping("/logoutAdmin")
    public List<Respuesta> logoutAdmin(HttpSession sessionHttp) throws SistemaLoginException {
        AdminSesion sesion = (AdminSesion) sessionHttp.getAttribute("admin");
        if (sesion != null) {
            Fachada.getInstancia().logoutAdmin(sesion);
            sessionHttp.removeAttribute("admin");
        }
        return Respuesta.lista(new Respuesta("paginaLogin", "login.html"));
    }

}
