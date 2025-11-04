package obligatorio.obli.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import obligatorio.obli.models.PropietarioSesion;
import obligatorio.obli.models.Usuarios.Propietario;

@RestController
@RequestMapping("/propietario")
public class PropietarioController {

    @RequestMapping("/vistaConectada")
    public List<Respuesta> vistaConectada(HttpSession sessionHttp) {
        PropietarioSesion sesion = (PropietarioSesion) sessionHttp.getAttribute("propietario");

        if (sesion == null) {
            return Respuesta.lista(new Respuesta("paginaLogin", "login.html"));
        }

        Propietario propietario = sesion.getPropietario();

        // Retornar datos del propietario
        return Respuesta.lista(
                new Respuesta("nombrePropietario", propietario.getNombre()),
                new Respuesta("datosPropietario", propietario));
    }

    @RequestMapping("/vistaCerrada")
    public void vistaCerrada(HttpSession sessionHttp) {
        // Opcional: lógica al cerrar la vista
    }
}
