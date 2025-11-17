package obligatorio.obli.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpSession;
import obligatorio.obli.ConexionNavegador;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

@RestController
@Scope("session")
@RequestMapping("/propietario")
public class PropietarioController implements Observador {

    private final ConexionNavegador conexionNavegador;
    private Propietario propietarioObservado;

    public PropietarioController(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    @RequestMapping("/vistaConectada")
    public List<Respuesta> vistaConectada(HttpSession sessionHttp) {
        Propietario propietario = (Propietario) sessionHttp.getAttribute(LoginController.SESSION_PROPIETARIO_COOKIE);

        if (propietario == null) {
            return Respuesta.lista(new Respuesta("paginaLogin", "login.html"));
        }

        if (propietarioObservado != propietario) {
            if (propietarioObservado != null) {
                propietarioObservado.quitarObservador(this);
            }
            propietario.agregarObservador(this);
            propietarioObservado = propietario;
        }

        return Respuesta.lista(
                new Respuesta("propietario", propietario));
    }

    @RequestMapping("/vistaCerrada")
    public void vistaCerrada(HttpSession sessionHttp) {
        if (propietarioObservado != null) {
            propietarioObservado.quitarObservador(this);
            propietarioObservado = null;
        }
        conexionNavegador.cerrarConexion();
    }

    @PostMapping("/notificaciones/borrar")
    public List<Respuesta> borrarNotificaciones(HttpSession sessionHttp) {
        Propietario propietario = (Propietario) sessionHttp.getAttribute(LoginController.SESSION_PROPIETARIO_COOKIE);

        if (propietario == null) {
            return Respuesta.lista(new Respuesta("error", "No hay sesión activa"));
        }

        propietario.borrarNotificaciones();
        return Respuesta.lista(new Respuesta("mensaje", "Notificaciones borradas exitosamente"));
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        try {
            if (propietarioObservado == null) {
                return;
            }

            if (evento.equals(Fachada.Eventos.nuevaNotificacion) ||
                    evento.equals(Fachada.Eventos.nuevaAsignacion) ||
                    evento.equals(Fachada.Eventos.cambioEstado) ||
                    evento.equals(Fachada.Eventos.nuevoTransito) ||
                    evento.equals(Fachada.Eventos.borradoNotificaciones)) {

                conexionNavegador.enviarJSON(Respuesta.lista(
                        new Respuesta("propietario", propietarioObservado)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
