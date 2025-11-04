package obligatorio.obli.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import obligatorio.obli.ConexionNavegador;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

@RestController
@RequestMapping("/administrador")
@Scope("session")
public class ControladorAdministrador implements Observador {

    private final ConexionNavegador conexionNavegador;

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

        if (admin == null) {

            return Respuesta.lista(
                    new Respuesta("paginaLogin", "login.html"));
        }

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

        if (evento.equals(Fachada.Eventos.nuevaAsignacion)) {
            conexionNavegador.enviarJSON(
                    Respuesta.lista(
                            new Respuesta("notificacion", "✅ Se asignó una nueva bonificación")));
            System.out.println("Notificación enviada via SSE: Nueva asignación");
        }
    }
}
