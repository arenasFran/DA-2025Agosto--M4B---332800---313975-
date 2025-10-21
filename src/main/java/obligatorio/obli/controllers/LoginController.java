package obligatorio.obli.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import obligatorio.obli.models.Sistemas.Fachada;
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
        // return Respuesta.lista(new Respuesta('login exitoso', "menu.html" ))

        return null;
    }

    @PostMapping("/admin")
    public List<Respuesta> loginAdmin(HttpSession sessionHttp, @RequestParam String ci, @RequestParam String password) {
        User user = Fachada.getInstancia().loginAdmin(ci, password);
        sessionHttp.setAttribute("user", "admin");

        // return Respuesta.lista(new Respuesta('login exitoso', "imagino que
        // adminPanel.html maybe" ))
        return null;
    }

}
