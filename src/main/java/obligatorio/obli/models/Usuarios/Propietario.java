package obligatorio.obli.models.Usuarios;

import java.util.List;

import obligatorio.obli.models.Vehiculo;

public class Propietario extends User {
    private List<Vehiculo> vehiculo;
    private double saldo;
    private double saldoMinAlerta;
}
