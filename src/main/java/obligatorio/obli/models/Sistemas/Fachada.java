package obligatorio.obli.models.Sistemas;

import java.util.List;

import obligatorio.obli.exceptions.propietario.PropietarioErrorActualizacionEstadoException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;
import obligatorio.obli.exceptions.propietario.estados.EstadoProhibidoRecibirBonificacionException;
import obligatorio.obli.exceptions.puesto.PuestoNoEncontradoException;
import obligatorio.obli.exceptions.bonificaciones.BonificacionNoEncontradaException;
import obligatorio.obli.exceptions.login.LoginCredencialesInvalidasException;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.PropietarioSesion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Bonificaciones.Bonificacion;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;

public class Fachada extends Observable {
    private SistemaLogin sistemaLogin;
    private SistemaUsuario sistemaUsuario;
    private SistemaBonificacion sistemaBonificacion;
    private SistemaPuesto sistemaPuesto;

    private Fachada() {
        sistemaLogin = SistemaLogin.getInstancia();
        sistemaUsuario = SistemaUsuario.getInstancia();
        sistemaBonificacion = SistemaBonificacion.getInstancia();
        sistemaPuesto = SistemaPuesto.getInstancia();
    }

    private static Fachada instancia;

    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    public enum Eventos {
        nuevaAsignacion
    }

    public PropietarioSesion loginPropietario(String ci, String password) throws LoginCredencialesInvalidasException {
        return this.sistemaLogin.loginPropietario(ci, password);
    }

    public AdminSesion loginAdmin(String ci, String password) throws LoginCredencialesInvalidasException {
        return this.sistemaLogin.loginAdmin(ci, password);
    }

    public Propietario buscarPropietarioPorCi(String ci) throws PropietarioNoEncontradoException {
        return this.sistemaUsuario.getPropietarioPorCi(ci);
    }

    public List<Bonificacion> mostrarBonificaciones() {
        return this.sistemaBonificacion.getBonificacions();
    }

    public List<Puesto> getPuestos() {
        return this.sistemaPuesto.getPuestos();
    }

    public void asignarBonificacion(String ci, String nombreBonificacion, String nombrePuesto)
            throws PropietarioNoEncontradoException, BonificacionNoEncontradaException, PuestoNoEncontradoException,
            EstadoProhibidoRecibirBonificacionException {
        Propietario propietario = this.buscarPropietarioPorCi(ci);
        Bonificacion bonificacion = this.sistemaBonificacion.buscarPorNombre(nombreBonificacion);
        Puesto puesto = this.sistemaPuesto.buscarPorNombre(nombrePuesto);

        propietario.asignarBonificacion(bonificacion, puesto);

        avisar(Eventos.nuevaAsignacion);
    }

    public void cambiarEstadoPropietario(String ci, String nuevoEstadoNombre)
            throws PropietarioNoEncontradoException, PropietarioErrorActualizacionEstadoException {
        Propietario propietario = this.buscarPropietarioPorCi(ci);

        Estado nuevoEstado = Estado.fromNombre(nuevoEstadoNombre);
        propietario.cambiarEstado(nuevoEstado);

        // Mandar notificacion al propietario
        // "[Fecha y hora] Se ha cambiado tu estado en el sistema. Tu estado actual es
        // [estado]"
    }

    public void logoutPropietario(PropietarioSesion sesion) {
        this.sistemaLogin.logoutPropietario(sesion);
    }

    public void logoutAdmin(AdminSesion sesion) {
        this.sistemaLogin.logoutAdmin(sesion);
    }
}
