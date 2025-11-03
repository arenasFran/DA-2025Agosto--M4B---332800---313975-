package obligatorio.obli.controllers;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import obligatorio.obli.dtos.PropietarioEstadoDTO;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Propietario;

/**
 * Session-scoped controller for admin operations on owner estado (use case 5.6 - Change Owner State).
 * Maintains propietarioActual in session state for page refresh resilience.
 */
@RestController
@Scope("session")
public class EstadoController {

    // Session state: currently searched propietario for cambiar-estado use case
    private Propietario propietarioActual;

    /**
     * Endpoint to search for a propietario by cedula.
     * Called when admin clicks "Buscar" button in cambiar-estado page.
     */
    @PostMapping("/estado/buscar")
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
     * Endpoint to change a propietario's estado.
     * Called when admin clicks "Cambiar estado" button.
     */
    @PutMapping("/estado/actualizar")
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
     * Endpoint called on page load/refresh to restore session state.
     * If a propietario was previously searched, it's returned so the UI can restore.
     */
    @RequestMapping(value = "/estado/vistaConectada", method = { RequestMethod.GET, RequestMethod.POST })
    public List<Respuesta> vistaConectada(
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
