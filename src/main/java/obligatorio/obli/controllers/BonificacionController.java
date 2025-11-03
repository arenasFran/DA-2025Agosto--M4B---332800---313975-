package obligatorio.obli.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import obligatorio.obli.exceptions.PropietarioNoEncontradoException;
import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Bonificacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Propietario;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/bonificaciones")
public class BonificacionController {

    @GetMapping("/get-bon")
    public List<Bonificacion> getBonificaciones() {
        return Fachada.getInstancia().mostrarBonificaciones();
    }

    @GetMapping("/get-puestos")
    public List<Puesto> getPuestos() {
        return Fachada.getInstancia().getPuestos();
    }

    @GetMapping("/get-propietario")
    public Propietario getPropietario(@RequestParam String ci) {
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(ci);
        if (propietario == null) {
            throw new PropietarioNoEncontradoException(ci);
        }
        return propietario;
    }

    @GetMapping("/get-asignaciones")
    public List<Asignacion> getAsignacionesPorPropietario(@RequestParam String ci) {
        return Fachada.getInstancia().getAsignacionesPorPropietario(ci);
    }

    @PostMapping("/asignar")
    public List<Respuesta> asignarBonificacion(
            @RequestParam String ci,
            @RequestParam String nombreBonificacion,
            @RequestParam String nombrePuesto) {
        Fachada.getInstancia().asignarBonificacion(ci, nombreBonificacion, nombrePuesto);
        return Respuesta.lista(
                new Respuesta("asignacion exitosa", "Bonificación asignada correctamente"));
    }

}
