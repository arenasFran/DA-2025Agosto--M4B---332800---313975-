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
    public List<Respuesta> getBonificaciones() {
        List<Bonificacion> bonificaciones = Fachada.getInstancia().mostrarBonificaciones();
        return Respuesta.lista(new Respuesta("bonificaciones", bonificaciones));
    }

    @GetMapping("/get-puestos")
    public List<Respuesta> getPuestos() {
        List<Puesto> puestos = Fachada.getInstancia().getPuestos();
        return Respuesta.lista(new Respuesta("puestos", puestos));
    }

    @GetMapping("/get-propietario")
    public List<Respuesta> getPropietario(@RequestParam String ci) throws PropietarioNoEncontradoException {
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(ci);
        return Respuesta.lista(new Respuesta("propietario", propietario));
    }

    @GetMapping("/get-asignaciones")
    public List<Respuesta> getAsignacionesPorPropietario(@RequestParam String ci)
            throws PropietarioNoEncontradoException {
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(ci);
        List<Asignacion> asignaciones = propietario.getAsignaciones();
        return Respuesta.lista(new Respuesta("asignaciones", asignaciones));
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

        // Restaurar propietario previo si existe (útil para reconexiones)
        if (this.propietarioActualCi != null) {
            System.out.println("Restaurando propietario: " + this.propietarioActualCi);
            try {
                Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(this.propietarioActualCi);
                respuestas.add(new Respuesta("propietario", propietario));
            } catch (PropietarioNoEncontradoException e) {
                System.err.println("Error al restaurar propietario: " + e.getMessage());
                respuestas.add(new Respuesta("mensaje", "Propietario no encontrado"));
            }
        } else {
            System.out.println("Vista de bonificaciones conectada - Sin propietario seleccionado");
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
