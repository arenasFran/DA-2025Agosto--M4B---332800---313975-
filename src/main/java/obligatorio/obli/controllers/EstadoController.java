package obligatorio.obli.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import obligatorio.obli.ConexionNavegador;
import obligatorio.obli.dtos.PropietarioEstadoDTO;
import obligatorio.obli.exceptions.PropietarioException;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

@RestController
@Scope("session")
public class EstadoController implements Observador {
    private Propietario propietarioActual;
    private final ConexionNavegador conexionNavegador;

    public EstadoController(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/estado/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    @PostMapping("/estado/buscar")
    public List<Respuesta> buscarPropietario(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String cedula) throws PropietarioException {

        if (this.propietarioActual != null) {
            this.propietarioActual.quitarObservador(this);
        }

        try {
            Propietario p = Fachada.getInstancia().buscarPropietarioPorCi(cedula);
            this.propietarioActual = p;
            p.agregarObservador(this);

            PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                    p.getCi(),
                    p.getNombre(),
                    p.getEstado().getNombre());

            return Respuesta.lista(new Respuesta("propietario", dto));
        } catch (PropietarioException e) {
            this.propietarioActual = null;
            throw e;
        }
    }

    @PutMapping("/estado/actualizar")
    public List<Respuesta> cambiarEstado(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String cedula,
            @RequestParam String nuevoEstado)
            throws PropietarioException {
        Propietario p = Fachada.getInstancia().buscarPropietarioPorCi(cedula);
        Estado e = Fachada.getInstancia().buscarEstadoPorNombre(nuevoEstado);
        p.cambiarEstado(e);

        return Respuesta.lista(
                new Respuesta("estadoCambiado", "Estado cambiado correctamente"));
    }

    @RequestMapping(value = "/estado/vistaConectada", method = { RequestMethod.GET, RequestMethod.POST })
    public List<Respuesta> vistaConectada(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin) {
        List<Respuesta> respuestas = new ArrayList<>();

        if (this.propietarioActual != null) {
            this.propietarioActual.agregarObservador(this);
            try {
                PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                        propietarioActual.getCi(),
                        propietarioActual.getNombre(),
                        propietarioActual.getEstado().getNombre());
                respuestas.add(new Respuesta("propietario", dto));
            } catch (Exception e) {
                respuestas.add(new Respuesta("mensaje", "Propietario no encontrado"));
            }
        }

        respuestas.add(new Respuesta("mensaje", "Vista conectada"));
        return respuestas;
    }

    @RequestMapping(value = "/estado/vistaCerrada", method = { RequestMethod.GET, RequestMethod.POST })
    public void vistaCerrada() {
        if (this.propietarioActual != null) {
            this.propietarioActual.quitarObservador(this);
        }
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (origen == this.propietarioActual && evento.equals(Fachada.Eventos.cambioEstado)) {
            conexionNavegador.enviarJSON(Respuesta.lista(propietarioActual()));
        }
    }

    private Respuesta propietarioActual() {
        if (this.propietarioActual != null) {
            try {
                PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                        propietarioActual.getCi(),
                        propietarioActual.getNombre(),
                        propietarioActual.getEstado().getNombre());
                return new Respuesta("propietario", dto);
            } catch (Exception e) {
                return new Respuesta("error", "Propietario no encontrado");
            }
        }
        return new Respuesta("mensaje", "Sin propietario seleccionado");
    }
}
