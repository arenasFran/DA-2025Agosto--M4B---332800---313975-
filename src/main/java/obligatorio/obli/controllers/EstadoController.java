package obligatorio.obli.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import obligatorio.obli.ConexionNavegador;
import obligatorio.obli.dtos.PropietarioEstadoDTO;
import obligatorio.obli.exceptions.propietario.PropietarioErrorActualizacionEstadoException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

@RestController
@Scope("session")
public class EstadoController implements Observador {
    private String propietarioActualCi;
    private final ConexionNavegador conexionNavegador;

    public EstadoController(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @PostMapping("/estado/buscar")
    public List<Respuesta> buscarPropietario(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String cedula) throws PropietarioNoEncontradoException {

        Propietario p = Fachada.getInstancia().buscarPropietarioPorCi(cedula);

        this.propietarioActualCi = p.getCi();

        PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                p.getCi(),
                p.getNombre(),
                p.getEstado().getNombre());

        return Respuesta.lista(new Respuesta("propietario", dto));
    }

    @PutMapping("/estado/actualizar")
    public List<Respuesta> cambiarEstado(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String cedula,
            @RequestParam String nuevoEstado)
            throws PropietarioNoEncontradoException, PropietarioErrorActualizacionEstadoException {
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

        Fachada.getInstancia().agregarObservador(this);

        if (this.propietarioActualCi != null) {
            try {
                Propietario p = Fachada.getInstancia().buscarPropietarioPorCi(this.propietarioActualCi);
                PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                        p.getCi(),
                        p.getNombre(),
                        p.getEstado().getNombre());
                respuestas.add(new Respuesta("propietario", dto));
            } catch (PropietarioNoEncontradoException e) {
                respuestas.add(new Respuesta("mensaje", "Propietario no encontrado"));
            }
        }

        respuestas.add(new Respuesta("mensaje", "Vista conectada"));
        return respuestas;
    }

    @RequestMapping(value = "/estado/vistaCerrada", method = { RequestMethod.GET, RequestMethod.POST })
    public void vistaCerrada() {
        Fachada.getInstancia().quitarObservador(this);
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (evento.equals(Fachada.Eventos.cambioEstado)) {
            conexionNavegador.enviarJSON(Respuesta.lista(propietarioActual()));
        }
    }

    private Respuesta propietarioActual() {
        if (this.propietarioActualCi != null) {
            try {
                Propietario p = Fachada.getInstancia().buscarPropietarioPorCi(this.propietarioActualCi);
                PropietarioEstadoDTO dto = new PropietarioEstadoDTO(
                        p.getCi(),
                        p.getNombre(),
                        p.getEstado().getNombre());
                return new Respuesta("propietario", dto);
            } catch (PropietarioNoEncontradoException e) {
                return new Respuesta("error", "Propietario no encontrado");
            }
        }
        return new Respuesta("mensaje", "Sin propietario seleccionado");
    }
}
