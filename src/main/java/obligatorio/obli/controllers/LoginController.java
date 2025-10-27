package obligatorio.obli.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.User;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping("/propietario")
    public List<Respuesta> loginPropietario(HttpSession sessionHttp, @RequestParam String ci,
            @RequestParam String password) {
        User user = Fachada.getInstancia().loginPropietario(ci, password);

        sessionHttp.setAttribute("user", user);
        return Respuesta.lista(new Respuesta("login exitoso", "menu.html"));

    }

    @PostMapping("/admin")
    public List<Respuesta> loginAdmin(HttpSession sessionHttp, @RequestParam String ci, @RequestParam String password) {
        User user = Fachada.getInstancia().loginAdmin(ci, password);
        sessionHttp.setAttribute("user", "admin");

        return Respuesta.lista(new Respuesta("login exitoso", "adminPanel.html"));
    }

    // @PostMapping("/loginAgenda")
    // public List<Respuesta> loginAgenda(HttpSession sesionHttp, @RequestParam
    // String username,
    // @RequestParam String password) throws AgendaException {

    // // login al modelo
    // Sesion sesion = Fachada.getInstancia().loginAgenda(username, password);

    // // si hay una sesion activa la cierro
    // logoutAgenda(sesionHttp);

    // // guardo la sesion de la logica en la sesionHttp
    // sesionHttp.setAttribute("usuarioAgenda", sesion);
    // return Respuesta.lista(new Respuesta("loginExitoso", "menu-agenda.html"));
    // }

    // @PostMapping("/loginAdmin")
    // public List<Respuesta> loginAdministrador(HttpSession sesionHttp,
    // @RequestParam String username,
    // @RequestParam String password) throws AgendaException {
    // // login al modelo
    // Administrador admin = Fachada.getInstancia().loginAdministrador(username,
    // password);

    // // guardo el admin en la sesionHttp
    // sesionHttp.setAttribute("usuarioAdmin", admin);
    // return Respuesta.lista(new Respuesta("loginExitoso",
    // "monitor-actividad.html"));
    // }

    // @PostMapping("/logoutAgenda")
    // public List<Respuesta> logoutAgenda(HttpSession sesionHttp) throws
    // AgendaException {
    // Sesion sesion = (Sesion) sesionHttp.getAttribute("usuarioAgenda");
    // if (sesion != null) {
    // Fachada.getInstancia().logout(sesion);
    // sesionHttp.removeAttribute("usuarioAgenda");
    // }
    // return Respuesta.lista(new Respuesta("paginaLogin", "login-agenda.html"));

    // }

}
