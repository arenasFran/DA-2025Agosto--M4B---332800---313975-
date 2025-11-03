package obligatorio.obli.controllers;

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
import obligatorio.obli.dtos.PropietarioEstadoDTO;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

@RestController
@RequestMapping("/administrador")
@Scope("session") // Una instancia por sesión - importante para mantener el estado del observador
public class ControladorAdministrador implements Observador {

    private final ConexionNavegador conexionNavegador;

    // Session state for cambiar-estado use case: currently searched propietario
    private Propietario propietarioActual;

    public ControladorAdministrador(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    @RequestMapping(value = "/vistaConectada", method = { RequestMethod.GET, RequestMethod.POST })
    public List<Respuesta> inicializarVista(
            @SessionAttribute(name = "admin", required = false) AdminSesion admin) {
        // Verificar si hay sesión de admin
        if (admin == null) {
            // Si no hay sesión, retornar redirección al login
            return Respuesta.lista(
                    new Respuesta("paginaLogin", "login.html"));
        }

        // Registrarse como observador para recibir notificaciones
        Fachada.getInstancia().agregarObservador(this);

        return Respuesta.lista(
                new Respuesta("mensaje", "Vista conectada - Recibirás notificaciones en tiempo real"),
                new Respuesta("nombreAdmin", admin.getAdmnin().getNombre()));
    }

    @RequestMapping(value = "/vistaCerrada", method = { RequestMethod.GET, RequestMethod.POST })
    public void vistaCerrada() {
        Fachada.getInstancia().quitarObservador(this);
        conexionNavegador.cerrarConexion();
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        // Si el evento es una nueva asignación, enviar notificación via SSE
        if (evento.equals(Fachada.Eventos.nuevaAsignacion)) {
            conexionNavegador.enviarJSON(
                    Respuesta.lista(
                            new Respuesta("notificacion", "✅ Se asignó una nueva bonificación")));
            System.out.println("Notificación enviada via SSE: Nueva asignación");
        }
    }

    // ===== Cambiar Estado Endpoints =====

    /**
     * Endpoint to search for a propietario by cedula (cambiar-estado use case).
     * Called when admin clicks "Buscar" button.
     */
    @PostMapping("/buscarPropietario")
    public List<Respuesta> buscarPropietario(
            @SessionAttribute(name = "admin", required = false) AdminSesion admin,
            @RequestParam String cedula) {

        // Verify admin session exists
        if (admin == null) {
            return Respuesta.lista(new Respuesta("paginaLogin", "login.html"));
        }

        // Search for propietario via Fachada
        Propietario p = Fachada.getInstancia().buscarPropietarioPorCi(cedula);

        // buscarPropietarioPorCi returns null if not found, but we want to throw exception
        // to trigger GlobalExceptionHandler which will return the proper error message
        if (p == null) {
            throw new obligatorio.obli.exceptions.PropietarioNoEncontradoException(cedula);
        }

        // Store in session for page refresh
        this.propietarioActual = p;

        // Create DTO with propietario info
        PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                p.getCi(),
                p.getNombre(),
                p.getEstado().getNombre()
        );

        // Return response for vistaWeb.js to call mostrar_propietarioEncontrado(dto)
        return Respuesta.lista(new Respuesta("propietarioEncontrado", dto));
    }

    /**
     * Endpoint to change a propietario's estado (cambiar-estado use case).
     * Called when admin clicks "Cambiar estado" button.
     */
    @PostMapping("/cambiarEstado")
    public List<Respuesta> cambiarEstado(
            @SessionAttribute(name = "admin", required = false) AdminSesion admin,
            @RequestParam String cedula,
            @RequestParam String nuevoEstado) {

        // Verify admin session exists
        if (admin == null) {
            return Respuesta.lista(new Respuesta("paginaLogin", "login.html"));
        }

        // Delegate to Fachada which orchestrates the estado change
        Fachada.getInstancia().cambiarEstadoPropietario(cedula, nuevoEstado);

        // Return success response for vistaWeb.js to call mostrar_estadoCambiado(message)
        return Respuesta.lista(
                new Respuesta("estadoCambiado", "Estado cambiado correctamente")
        );
    }

    /**
     * Endpoint called on page load/refresh to restore session state for cambiar-estado use case.
     * If a propietario was previously searched, it's returned so the UI can restore.
     */
    @RequestMapping(value = "/cambiarEstado/vistaConectada", method = { RequestMethod.GET, RequestMethod.POST })
    public List<Respuesta> vistaConectadaCambiarEstado(
            @SessionAttribute(name = "admin", required = false) AdminSesion admin) {

        // Verify admin session exists
        if (admin == null) {
            return Respuesta.lista(new Respuesta("paginaLogin", "login.html"));
        }

        // If there's a propietario in session state, restore it
        if (this.propietarioActual != null) {
            PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                    this.propietarioActual.getCi(),
                    this.propietarioActual.getNombre(),
                    this.propietarioActual.getEstado().getNombre()
            );
            return Respuesta.lista(new Respuesta("propietarioEncontrado", dto));
        }

        // No state to restore - empty response
        return Respuesta.lista(new Respuesta("mensaje", "Vista conectada"));
    }
}
