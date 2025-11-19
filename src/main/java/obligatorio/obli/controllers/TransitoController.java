package obligatorio.obli.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import obligatorio.obli.ConexionNavegador;
import obligatorio.obli.dtos.DetalleTransitoDTO;
import obligatorio.obli.dtos.TarifaDTO;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Transito;
import obligatorio.obli.models.Sistemas.Fachada;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;
import obligatorio.obli.observador.Observador;

@RestController
@RequestMapping("/transitos")
@Scope("session")
public class TransitoController implements Observador {

    private List<DetalleTransitoDTO> transitosEmulados;
    private int ultimoTamañoConocido;
    private Propietario propietarioActual;
    private final ConexionNavegador conexionNavegador;

    public TransitoController(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
        this.transitosEmulados = new ArrayList<>();
        this.ultimoTamañoConocido = 0;
    }

    @GetMapping(value = "/transitos/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    @GetMapping("/puestos")
    public List<Respuesta> getPuestos(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin) {

        List<Puesto> puestos = Fachada.getInstancia().getPuestos();
        return Respuesta.lista(new Respuesta("puestos", puestos));
    }

    @GetMapping("/tarifas")
    public List<Respuesta> getTarifasPorPuesto(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String nombrePuesto) throws Exception {

        Puesto puesto = Fachada.getInstancia().buscarPuestoPorNombre(nombrePuesto);

        List<TarifaDTO> tarifas = puesto.getTarifas().stream()
                .map(tarifa -> new TarifaDTO(tarifa))
                .collect(Collectors.toList());

        return Respuesta.lista(new Respuesta("tarifas", tarifas));
    }

    @PostMapping("/emular")
    public List<Respuesta> emularTransito(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin,
            @RequestParam String matricula,
            @RequestParam String nombrePuesto,
            @RequestParam String fechaHora) throws Exception {
        if (this.propietarioActual != null) {
            this.propietarioActual.quitarObservador(this);
        }

        try {
            Propietario propietario = Fachada.getInstancia().buscarPropietarioDeVehiculo(
                    Fachada.getInstancia().buscarVehiculoPorMatricula(matricula));

            this.propietarioActual = propietario;
            propietario.agregarObservador(this);

            Transito nuevo = propietario.crearTransito(matricula, nombrePuesto, fechaHora);
            DetalleTransitoDTO detalle = new DetalleTransitoDTO(nuevo, propietario);

            this.transitosEmulados.add(detalle);
            this.ultimoTamañoConocido = this.transitosEmulados.size();

            return Respuesta.lista(new Respuesta("transito_emulado", detalle));
        } catch (Exception e) {
            this.propietarioActual = null;
            throw e;
        }
    }

    @PostMapping("/vistaConectada")
    public List<Respuesta> vistaConectada(
            @SessionAttribute(name = LoginController.SESSION_ADMIN_COOKIE, required = true) Administrador admin) {

        if (this.propietarioActual != null) {
            this.propietarioActual.agregarObservador(this);
        }

        List<Respuesta> respuestas = new ArrayList<>();

        if (!this.transitosEmulados.isEmpty()) {
            respuestas.add(new Respuesta("historial_transitos", this.transitosEmulados));
        }

        respuestas.add(new Respuesta("mensaje", "Vista conectada"));
        return respuestas;
    }

    @PostMapping("/vistaCerrada")
    public void vistaCerrada() {
        if (this.propietarioActual != null) {
            this.propietarioActual.quitarObservador(this);
        }
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (origen == this.propietarioActual && evento.equals(Fachada.Eventos.nuevoTransito)) {
            conexionNavegador.enviarJSON(Respuesta.lista(ultimoTransito()));
        }
    }

    private Respuesta ultimoTransito() {
        int tamañoActual = this.transitosEmulados.size();
        if (tamañoActual == this.ultimoTamañoConocido) {
            return new Respuesta("mensaje", "Sin cambios");
        }

        if (!this.transitosEmulados.isEmpty()) {
            DetalleTransitoDTO ultimoDetalle = this.transitosEmulados.get(this.transitosEmulados.size() - 1);
            this.ultimoTamañoConocido = tamañoActual;
            return new Respuesta("transito_emulado", ultimoDetalle);
        }

        return new Respuesta("mensaje", "Sin tránsitos");
    }
}
