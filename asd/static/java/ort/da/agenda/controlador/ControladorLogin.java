package ort.da.agenda.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import ort.da.agenda.modelo.Administrador;
import ort.da.agenda.modelo.AgendaException;
import ort.da.agenda.modelo.Fachada;
import ort.da.agenda.modelo.Sesion;

@RestController
@RequestMapping("/acceso")

public class ControladorLogin {

    @PostMapping("/loginAgenda")
    public List<Respuesta> loginAgenda(HttpSession sesionHttp, @RequestParam String username, @RequestParam String password) throws AgendaException {
        
        //login al modelo
        Sesion sesion  = Fachada.getInstancia().loginAgenda(username, password);
        

        //si hay una sesion activa la cierro
        logoutAgenda(sesionHttp);

        //guardo la sesion de la logica en la sesionHttp
        sesionHttp.setAttribute("usuarioAgenda", sesion);
        return Respuesta.lista(new Respuesta("loginExitoso", "menu-agenda.html"));
    }

    @PostMapping("/loginAdmin")
    public List<Respuesta> loginAdministrador(HttpSession sesionHttp, @RequestParam String username, @RequestParam String password) throws AgendaException {
        //login al modelo
        Administrador admin = Fachada.getInstancia().loginAdministrador(username, password);
                
        //guardo el admin en la sesionHttp
        sesionHttp.setAttribute("usuarioAdmin", admin);
        return Respuesta.lista(new Respuesta("loginExitoso", "monitor-actividad.html"));
    }

    @PostMapping("/logoutAgenda")
    public List<Respuesta> logoutAgenda(HttpSession sesionHttp) throws AgendaException {
        Sesion sesion = (Sesion)sesionHttp.getAttribute("usuarioAgenda");
        if(sesion!=null){
            Fachada.getInstancia().logout(sesion);
            sesionHttp.removeAttribute("usuarioAgenda");
        }
        return Respuesta.lista(new Respuesta("paginaLogin", "login-agenda.html"));

    }

}
