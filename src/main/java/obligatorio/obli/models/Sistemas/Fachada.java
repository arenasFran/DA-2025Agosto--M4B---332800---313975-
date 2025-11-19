package obligatorio.obli.models.Sistemas;

import java.util.List;

import obligatorio.obli.exceptions.PropietarioException;
import obligatorio.obli.exceptions.BonificacionException;
import obligatorio.obli.exceptions.PuestoException;
import obligatorio.obli.exceptions.LoginException;
import obligatorio.obli.exceptions.AdministradorException;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Vehiculo;
import obligatorio.obli.models.Bonificaciones.Bonificacion;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;

public class Fachada {
    private SistemaUsuario sistemaUsuario;
    private SistemaBonificacion sistemaBonificacion;
    private SistemaPuesto sistemaPuesto;

    private Fachada() {
        sistemaUsuario = new SistemaUsuario();
        sistemaBonificacion = new SistemaBonificacion();
        sistemaPuesto = new SistemaPuesto();
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

    public Propietario loginPropietario(String ci, String password) throws LoginException {
        try {
            Propietario propietario = this.sistemaUsuario.getPropietarioPorCi(ci);
            if (!propietario.getPassword().equals(password)) {
                throw new LoginException("Credenciales inválidas.");
            }
            return propietario;
        } catch (PropietarioException e) {
            throw new LoginException("Credenciales inválidas.");
        }
    }

    public Administrador loginAdmin(String ci, String password) throws LoginException {
        try {
            Administrador admin = this.sistemaUsuario.getAdministradorPorCi(ci);
            if (!admin.getPassword().equals(password)) {
                throw new LoginException("Credenciales inválidas.");
            }
            return admin;
        } catch (AdministradorException e) {
            throw new LoginException("Credenciales inválidas.");
        }
    }

    public Propietario buscarPropietarioPorCi(String ci) throws PropietarioException {
        return this.sistemaUsuario.getPropietarioPorCi(ci);
    }

    public List<Bonificacion> mostrarBonificaciones() {
        return this.sistemaBonificacion.getBonificacions();
    }

    public List<Puesto> getPuestos() {
        return this.sistemaPuesto.getPuestos();
    }

    public Bonificacion buscarBonificacionPorNombre(String nombreBonificacion)
            throws BonificacionException {
        return this.sistemaBonificacion.buscarPorNombre(nombreBonificacion);
    }

    public Puesto buscarPuestoPorNombre(String nombrePuesto) throws PuestoException {
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
