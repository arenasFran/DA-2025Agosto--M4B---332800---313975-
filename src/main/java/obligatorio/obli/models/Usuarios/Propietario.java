package obligatorio.obli.models.Usuarios;

import java.util.List;

import obligatorio.obli.models.Vehiculo;

public class Propietario extends User {
    private List<Vehiculo> vehiculo;
    private double saldo;
    private double saldoMinAlerta;

    public Propietario(int id, String ci, String nombre, String password, List<Vehiculo> vehiculo, double saldo, double saldoMinAlerta) {
        super(id, ci, nombre, password);
        this.vehiculo = vehiculo;
        this.saldo = saldo;
        this.saldoMinAlerta = saldoMinAlerta;
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
}
