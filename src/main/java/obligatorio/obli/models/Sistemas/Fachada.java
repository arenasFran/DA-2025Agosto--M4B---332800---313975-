package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.exceptions.PropietarioNoEncontradoException;
import obligatorio.obli.exceptions.SistemaLoginException;
import obligatorio.obli.models.AdminSesion;
import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Bonificacion;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.PropietarioSesion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.models.Usuarios.User;
import obligatorio.obli.observador.Observable;

public class Fachada extends Observable {

    /**
     * Enum con los tipos de eventos que notifica la Fachada
     */
    public enum Eventos {
        nuevaAsignacion
    }

    private static Fachada instancia;

    private Fachada() {

    }

    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    public PropietarioSesion loginPropietario(String ci, String password) throws SistemaLoginException {
        SistemaLogin sistemaLogin = SistemaLogin.getInstancia();
        return sistemaLogin.loginPropietario(ci, password);
    }

    public AdminSesion loginAdmin(String ci, String password) throws SistemaLoginException {
        SistemaLogin sistemaLogin = SistemaLogin.getInstancia();
        return sistemaLogin.loginAdmin(ci, password);
    }

    public Propietario buscarPropietarioPorCi(String ci) {
        SistemaUsuario s = SistemaUsuario.getInstancia();
        return s.devolverPorpietarioPorCi(ci);
    }

    public List<Bonificacion> mostrarBonificaciones() {
        SistemaBonificacion s = SistemaBonificacion.getInstancia();
        return s.getBonificacions();
    }

    public List<Puesto> getPuestos() {
        SistemaPuesto s = SistemaPuesto.getInstancia();
        return s.getPuestos();
    }

    public void asignarBonificacion(String ci, String nombreBonificacion, String nombrePuesto) {
        Propietario p = SistemaUsuario.getInstancia().devolverPorpietarioPorCi(ci);
        if (p == null)
            throw new PropietarioNoEncontradoException(ci);

        Bonificacion bonificacion = SistemaBonificacion.getInstancia().buscarPorNombre(nombreBonificacion);
        if (bonificacion == null)
            throw new RuntimeException("Bonificación no encontrada");

        Puesto puesto = SistemaPuesto.getInstancia().buscarPorNombre(nombrePuesto);
        if (puesto == null)
            throw new RuntimeException("Puesto no encontrado");

        SistemaAsignacion.getInstancia().asignarBonificacion(p, bonificacion, puesto);

        // NOTIFICAR A TODOS LOS OBSERVADORES (ADMINISTRADORES CONECTADOS)
        avisar(Eventos.nuevaAsignacion);
    }

    public List<Asignacion> getAsignacionesPorPropietario(String ci) {
        // Obtener directamente del propietario (más orientado a objetos)
        Propietario propietario = buscarPropietarioPorCi(ci);
        if (propietario != null) {
            return propietario.getAsignaciones();
        }
        return new ArrayList<>();
    }

    public void cambiarEstadoPropietario(String ci, String nuevoEstadoNombre)
            throws RuntimeException, PropietarioNoEncontradoException {
        Propietario p = SistemaUsuario.getInstancia().devolverPorpietarioPorCi(ci);
        if (p == null)
            throw new PropietarioNoEncontradoException(ci);

        Estado nuevoEstado = Estado.fromNombre(nuevoEstadoNombre);
        p.cambiarEstado(nuevoEstado);

        // Mandar notificacion al propietario
        // "[Fecha y hora] Se ha cambiado tu estado en el sistema. Tu estado actual es
        // [estado]"
    }

    public void logoutPropietario(PropietarioSesion sesion) {
        SistemaLogin.getInstancia().logoutPropietario(sesion);
    }

    public void logoutAdmin(AdminSesion sesion) {
        SistemaLogin.getInstancia().logoutAdmin(sesion);
    }
}
