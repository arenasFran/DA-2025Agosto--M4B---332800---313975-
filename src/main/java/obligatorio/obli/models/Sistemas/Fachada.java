package obligatorio.obli.models.Sistemas;

import java.util.List;

import obligatorio.obli.exceptions.propietario.PropietarioErrorActualizacionEstadoException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;
import obligatorio.obli.exceptions.propietario.estados.EstadoProhibidoRecibirBonificacionException;
import obligatorio.obli.exceptions.puesto.PuestoNoEncontradoException;
import obligatorio.obli.exceptions.bonificaciones.BonificacionNoEncontradaException;
import obligatorio.obli.exceptions.login.LoginCredencialesInvalidasException;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.ResultadoTransito;
import obligatorio.obli.models.Vehiculo;
import obligatorio.obli.models.Tarifa;
import obligatorio.obli.models.Transito;
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
        cambioEstado
    }

    public Propietario loginPropietario(String ci, String password) throws LoginCredencialesInvalidasException {
        try {
            return this.sistemaUsuario.getPropietarioPorCi(ci);
        } catch (PropietarioNoEncontradoException e) {
            throw new LoginCredencialesInvalidasException();
        }
    }

    public Administrador loginAdmin(String ci, String password) throws LoginCredencialesInvalidasException {
        try {
            return this.sistemaUsuario.getAdministradorPorCi(ci);
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

    // public void asignarBonificacion(String ci, String nombreBonificacion, String
    // nombrePuesto)
    // throws PropietarioNoEncontradoException, BonificacionNoEncontradaException,
    // PuestoNoEncontradoException,
    // EstadoProhibidoRecibirBonificacionException {
    // Puesto puesto = this.sistemaPuesto.buscarPorNombre(nombrePuesto);

    // propietario.asignarBonificacion(bonificacion, puesto);

    // avisar(Eventos.nuevaAsignacion);
    // }

    public Bonificacion buscarBonificacionPorNombre(String nombreBonificacion)
            throws BonificacionNoEncontradaException {
        return this.sistemaBonificacion.buscarPorNombre(nombreBonificacion);
    }

    public void cambiarEstadoPropietario(String ci, String nuevoEstadoNombre)
            throws PropietarioNoEncontradoException, PropietarioErrorActualizacionEstadoException {
        Propietario propietario = this.buscarPropietarioPorCi(ci);

        Estado nuevoEstado = Estado.fromNombre(nuevoEstadoNombre);
        propietario.cambiarEstado(nuevoEstado);
        avisar(Eventos.cambioEstado);

        // Mandar notificacion al propietario
        // "[Fecha y hora] Se ha cambiado tu estado en el sistema. Tu estado actual es
        // [estado]"
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

    public ResultadoTransito emularTransito(String matricula, String nombrePuesto, String fechaHora) throws Exception {
        Vehiculo vehiculo = this.sistemaUsuario.buscarVehiculoPorMatricula(matricula);
        Propietario propietario = this.sistemaUsuario.buscarPropietarioDeVehiculo(vehiculo);
        Puesto puesto = this.sistemaPuesto.buscarPorNombre(nombrePuesto);

        String categoriaVehiculo = vehiculo.getNombreCategoria();
        Tarifa tarifa = puesto.obtenerTarifaPorCategoria(categoriaVehiculo);
        Bonificacion bonificacion = propietario.obtenerBonificacionDelTransito(puesto);

        Transito transito = Transito.crearConFechaString(puesto, vehiculo, tarifa, fechaHora, bonificacion);
        double saldoAntes = propietario.getSaldo();
        double montoDescontado = propietario.registrarTransito(transito);
        propietario.enviarNotificacionesTransito(transito);
        double saldoDespues = propietario.getSaldo();

        avisar(Eventos.nuevoTransito);

        return new ResultadoTransito(transito, saldoAntes, saldoDespues, montoDescontado);
    }

}
