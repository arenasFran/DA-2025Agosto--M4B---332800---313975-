package obligatorio.obli.models.Usuarios;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.exceptions.propietario.PropietarioErrorActualizacionEstadoException;
import obligatorio.obli.exceptions.propietario.estados.EstadoProhibidoRecibirBonificacionException;
import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Vehiculo;
import obligatorio.obli.models.Bonificaciones.Bonificacion;

public class Propietario extends User {
    private List<Vehiculo> vehiculo;
    private double saldo;
    private double saldoMinAlerta;
    private Estado estado;
    private List<Asignacion> asignaciones;
    // private List<Transito> transitos;

    public Propietario(int id, String ci, String nombre, String password, List<Vehiculo> vehiculo, double saldo,
            double saldoMinAlerta, Estado estado) {
        super(id, ci, nombre, password);
        this.vehiculo = vehiculo;
        this.saldo = saldo;
        this.saldoMinAlerta = saldoMinAlerta;
        this.estado = estado;
        this.asignaciones = new ArrayList<>();
    }

    // Getters and setters
    public List<Vehiculo> getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(List<Vehiculo> vehiculo) {
        this.vehiculo = vehiculo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getSaldoMinAlerta() {
        return saldoMinAlerta;
    }

    public void setSaldoMinAlerta(double saldoMinAlerta) {
        this.saldoMinAlerta = saldoMinAlerta;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Asignacion> getAsignaciones() {
        return new ArrayList<>(asignaciones);
    }

    public void agregarAsignacion(Asignacion asignacion) {
        if (asignacion != null && !asignaciones.contains(asignacion)) {
            asignaciones.add(asignacion);
        }
    }

    public void asignarBonificacion(Bonificacion bonificacion, Puesto puesto)
            throws EstadoProhibidoRecibirBonificacionException {
        if (!this.puedeRecibirBonificacion()) {
            throw new EstadoProhibidoRecibirBonificacionException(this.estado);
        }

        Asignacion asignacion = new Asignacion(bonificacion, puesto);
        this.agregarAsignacion(asignacion);
    }

    public void removerAsignacion(Asignacion asignacion) {
        asignaciones.remove(asignacion);
    }

    public void cambiarEstado(Estado nuevoEstado) throws PropietarioErrorActualizacionEstadoException {
        if (this.estado.equals(nuevoEstado)) {
            throw new PropietarioErrorActualizacionEstadoException(
                    String.format("El propietario ya esta en el estado '%s'", nuevoEstado.getNombre()));
        }
        this.estado = nuevoEstado;
    }

    // Métodos delegados al Estado (Patrón State Expert)
    public boolean puedeIniciarSesion() {
        return this.estado.puedeIniciarSesion();
    }

    public boolean puedeRealizarTransito() {
        return this.estado.puedeRealizarTransito();
    }

    public boolean puedeRecibirBonificacion() {
        return this.estado.puedeRecibirBonificacion();
    }

    public boolean aplicanBonificacionesEnTransito() {
        return this.estado.aplicanBonificacionesEnTransito();
    }

    public boolean recibeNotificaciones() {
        return this.estado.recibeNotificaciones();
    }

    // public void registrarTransito(Transito transito) {
} // protected void hacerRegistroTransito(Transito transito) {
