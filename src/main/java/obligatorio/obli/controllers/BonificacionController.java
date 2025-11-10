package obligatorio.obli.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import obligatorio.obli.exceptions.bonificaciones.BonificacionNoEncontradaException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;
import obligatorio.obli.exceptions.propietario.estados.EstadoProhibidoRecibirBonificacionException;
import obligatorio.obli.exceptions.puesto.PuestoNoEncontradoException;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Bonificacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Propietario;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Scope("session")
@RequestMapping("/bonificaciones")
public class BonificacionController {

    private String propietarioActualCi;

    @GetMapping("/get-bon")
    public List<Bonificacion> getBonificaciones() {
        return Fachada.getInstancia().mostrarBonificaciones();
    }

    @GetMapping("/get-puestos")
    public List<Puesto> getPuestos() {
        return Fachada.getInstancia().getPuestos();
    }

    @GetMapping("/get-propietario")
    public Propietario getPropietario(@RequestParam String ci) throws PropietarioNoEncontradoException {
        return Fachada.getInstancia().buscarPropietarioPorCi(ci);
    }

    @GetMapping("/get-asignaciones")
    public List<Asignacion> getAsignacionesPorPropietario(@RequestParam String ci)
            throws PropietarioNoEncontradoException {
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(ci);
        return propietario.getAsignaciones();
    }

    @PostMapping("/buscar-propietario")
    public List<Respuesta> buscarPropietarioConAsignaciones(@RequestParam String ci)
            throws PropietarioNoEncontradoException {
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(ci);

        // Guardar la cédula del propietario en la sesión
        this.propietarioActualCi = ci;

        return Respuesta.lista(
                new Respuesta("propietario", propietario));
    }

    @RequestMapping(value = "/vistaConectada", method = { RequestMethod.GET, RequestMethod.POST })
    public List<Respuesta> vistaConectada(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) AdminSesion adminSession) {
        List<Respuesta> respuestas = new ArrayList<>();

        System.out.println("Propietario actual en bonificaciones: " + this.propietarioActualCi);

        if (this.propietarioActualCi != null) {
            try {
                Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(this.propietarioActualCi);
                respuestas.add(new Respuesta("propietario", propietario));
            } catch (PropietarioNoEncontradoException e) {
                respuestas.add(new Respuesta("mensaje", "Propietario no encontrado"));
            }
        }

        respuestas.add(new Respuesta("mensaje", "Vista conectada"));
        return respuestas;
    }

    @PostMapping("/asignar")
    public List<Respuesta> asignarBonificacion(
            @RequestParam String ci,
            @RequestParam String nombreBonificacion,
            @RequestParam String nombrePuesto)
            throws PropietarioNoEncontradoException,
            BonificacionNoEncontradaException,
            PuestoNoEncontradoException,
            EstadoProhibidoRecibirBonificacionException {
        Fachada.getInstancia().asignarBonificacion(ci, nombreBonificacion, nombrePuesto);

        return Respuesta.lista(
                new Respuesta("asignacion exitosa", "Bonificación asignada correctamente"));
    }

}
