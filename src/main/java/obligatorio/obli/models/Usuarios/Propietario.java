package obligatorio.obli.models.Usuarios;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Vehiculo;

public class Propietario extends User {
    private List<Vehiculo> vehiculo;
    private double saldo;
    private double saldoMinAlerta;
    private Estado estado;
    private List<Asignacion> asignaciones;
    // private boolean puedeLoguearse;
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

    public void removerAsignacion(Asignacion asignacion) {
        asignaciones.remove(asignacion);
    }

    // public void registrarTransito(Transito transito) {
} // protected void hacerRegistroTransito(Transito transito) {
