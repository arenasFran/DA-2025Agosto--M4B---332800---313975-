package obligatorio.obli.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import obligatorio.obli.ConexionNavegador;
import obligatorio.obli.exceptions.BonificacionException;
import obligatorio.obli.exceptions.PropietarioException;
import obligatorio.obli.exceptions.PuestoException;
import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Bonificaciones.Bonificacion;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

@RestController
@Scope("session")
@RequestMapping("/bonificaciones")
public class BonificacionController implements Observador {

    private Propietario propietarioActual;
    private final ConexionNavegador conexionNavegador;

    public BonificacionController(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    @GetMapping("/get-bon")
    public List<Respuesta> getBonificaciones(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin) {
        List<Bonificacion> bonificaciones = Fachada.getInstancia().mostrarBonificaciones();
        return Respuesta.lista(new Respuesta("bonificaciones", bonificaciones));
    }

    @GetMapping("/get-puestos")
    public List<Respuesta> getPuestos(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin) {
        List<Puesto> puestos = Fachada.getInstancia().getPuestos();
        return Respuesta.lista(new Respuesta("puestos", puestos));
    }

    @GetMapping("/get-asignaciones")
    public List<Respuesta> getAsignacionesPorPropietario(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String ci)
            throws PropietarioException {
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(ci);
        List<Asignacion> asignaciones = propietario.getAsignaciones();
        return Respuesta.lista(new Respuesta("asignaciones", asignaciones));
    }

    @PostMapping("/buscar-propietario")
    public List<Respuesta> buscarPropietarioConAsignaciones(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String ci)
            throws PropietarioException {
        if (this.propietarioActual != null) {
            this.propietarioActual.quitarObservador(this);
        }

        try {
            Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(ci);
            this.propietarioActual = propietario;
            propietario.agregarObservador(this);

            conexionNavegador.enviarJSON(Respuesta.lista(
                    new Respuesta("propietario", propietario)));

            return Respuesta.lista(
                    new Respuesta("propietario", propietario));
        } catch (PropietarioException e) {
            this.propietarioActual = null;
            throw e;
        }
    }

    @RequestMapping(value = "/vistaConectada", method = { RequestMethod.GET, RequestMethod.POST })
    public List<Respuesta> vistaConectada(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin) {
        List<Respuesta> respuestas = new ArrayList<>();

        if (this.propietarioActual != null) {
            this.propietarioActual.agregarObservador(this);
            try {
                Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCi(this.propietarioActual.getCi());
                respuestas.add(new Respuesta("propietario", propietario));
            } catch (Exception e) {
                respuestas.add(new Respuesta("mensaje", "Propietario no encontrado"));
            }
        }

        respuestas.add(new Respuesta("mensaje", "Vista conectada"));
        return respuestas;
    }

    @RequestMapping(value = "/vistaCerrada", method = { RequestMethod.GET, RequestMethod.POST })
    public void vistaCerrada() {
        if (this.propietarioActual != null) {
            this.propietarioActual.quitarObservador(this);
        }
    }

    @PostMapping("/asignar")
    public List<Respuesta> asignarBonificacion(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String ci,
            @RequestParam String nombreBonificacion,
            @RequestParam String nombrePuesto)
            throws PropietarioException,
            BonificacionException,
            PuestoException {
        Propietario p = Fachada.getInstancia().buscarPropietarioPorCi(ci);
        Bonificacion bonificacion = Fachada.getInstancia().buscarBonificacionPorNombre(nombreBonificacion);
        Puesto puesto = Fachada.getInstancia().buscarPuestoPorNombre(nombrePuesto);
        p.asignarBonificacion(bonificacion, puesto);

        return Respuesta.lista(
                new Respuesta("asignacion exitosa", "Bonificación asignada correctamente"));
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (origen == this.propietarioActual && evento.equals(Fachada.Eventos.nuevaAsignacion)) {
            conexionNavegador.enviarJSON(Respuesta.lista(propietarioActual()));
        }
    }

    private Respuesta propietarioActual() {
        if (this.propietarioActual != null) {
            try {
                return new Respuesta("propietario", propietarioActual);
            } catch (Exception e) {
                return new Respuesta("error", "Propietario no encontrado");
            }
        }
        return new Respuesta("mensaje", "Sin propietario seleccionado");
    }

}
