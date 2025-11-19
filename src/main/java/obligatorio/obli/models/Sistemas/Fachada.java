package obligatorio.obli.models.Sistemas;

import java.util.List;

import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;
import obligatorio.obli.exceptions.puesto.PuestoNoEncontradoException;
import obligatorio.obli.exceptions.bonificaciones.BonificacionNoEncontradaException;
import obligatorio.obli.exceptions.login.LoginCredencialesInvalidasException;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Vehiculo;
import obligatorio.obli.models.Bonificaciones.Bonificacion;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.observador.Observable;

public class Fachada extends Observable {
    private SistemaUsuario sistemaUsuario;
    private SistemaBonificacion sistemaBonificacion;
    private SistemaPuesto sistemaPuesto;

    private Fachada() {
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
        nuevaAsignacion,
        nuevoTransito,
        cambioEstado,
        nuevaNotificacion,
        borradoNotificaciones
    }

    public Propietario loginPropietario(String ci, String password) throws LoginCredencialesInvalidasException {
        try {
            Propietario propietario = this.sistemaUsuario.getPropietarioPorCi(ci);
            if (!propietario.getPassword().equals(password)) {
                throw new LoginCredencialesInvalidasException();
            }
            return propietario;
        } catch (PropietarioNoEncontradoException e) {
            throw new LoginCredencialesInvalidasException();
        }
    }

    public Administrador loginAdmin(String ci, String password) throws LoginCredencialesInvalidasException {
        try {
            Administrador admin = this.sistemaUsuario.getAdministradorPorCi(ci);
            if (!admin.getPassword().equals(password)) {
                throw new LoginCredencialesInvalidasException();
            }
            return admin;
        } catch (Exception e) {
            throw new LoginCredencialesInvalidasException();
        }
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

    public Bonificacion buscarBonificacionPorNombre(String nombreBonificacion)
            throws BonificacionNoEncontradaException {
        return this.sistemaBonificacion.buscarPorNombre(nombreBonificacion);
    }

    public Puesto buscarPuestoPorNombre(String nombrePuesto) throws PuestoNoEncontradoException {
        return this.sistemaPuesto.buscarPorNombre(nombrePuesto);
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) throws Exception {
        return this.sistemaUsuario.buscarVehiculoPorMatricula(matricula);
    }

    public Propietario buscarPropietarioDeVehiculo(Vehiculo vehiculo) throws Exception {
        return this.sistemaUsuario.buscarPropietarioDeVehiculo(vehiculo);
    }

    public Estado buscarEstadoPorNombre(String nombreEstado) {
        return Estado.fromNombre(nombreEstado);
    }
}
