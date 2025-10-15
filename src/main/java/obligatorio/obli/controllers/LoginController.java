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

    @PostMapping("/")
    public List<Respuesta> login(@RequestParam String username, @RequestParam String password) {
        User user = Fachada.getInstancia().Login(username, password);

        // return Respuesta.lista(new Respuesta('login exitoso', "menu.html" ))

        return null;
    }

}
