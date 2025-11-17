package obligatorio.obli.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import obligatorio.obli.ConexionNavegador;
import obligatorio.obli.exceptions.bonificaciones.BonificacionNoEncontradaException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;
import obligatorio.obli.exceptions.propietario.estados.EstadoProhibidoRecibirBonificacionException;
import obligatorio.obli.exceptions.puesto.PuestoNoEncontradoException;
import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Bonificaciones.Bonificacion;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Scope("session")
@RequestMapping("/bonificaciones")
public class BonificacionController implements Observador {

    private String propietarioActualCi;
    private final ConexionNavegador conexionNavegador;

    public BonificacionController(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

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

        this.propietarioActualCi = ci;

        return Respuesta.lista(
                new Respuesta("propietario", propietario));
    }

    @RequestMapping(value = "/vistaConectada", method = { RequestMethod.GET, RequestMethod.POST })
    public List<Respuesta> vistaConectada(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin) {
        List<Respuesta> respuestas = new ArrayList<>();
        Fachada.getInstancia().agregarObservador(this);

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

    @RequestMapping(value = "/vistaCerrada", method = { RequestMethod.GET, RequestMethod.POST })
    public void vistaCerrada() {
        Fachada.getInstancia().quitarObservador(this);
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

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (evento.equals(Fachada.Eventos.nuevaAsignacion)) {
            conexionNavegador.enviarJSON(Respuesta.lista(propietarioActual()));
        }
    }

    private Respuesta propietarioActual() {
        if (this.propietarioActualCi != null) {
            try {
                Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(this.propietarioActualCi);
                return new Respuesta("propietario", propietario);
            } catch (PropietarioNoEncontradoException e) {
                return new Respuesta("error", "Propietario no encontrado");
            }
        }
        return new Respuesta("mensaje", "Sin propietario seleccionado");
    }

}
