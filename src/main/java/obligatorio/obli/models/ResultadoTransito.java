package obligatorio.obli.models;

/**
 * Clase que encapsula el resultado de un tránsito emulado
 * incluyendo el saldo antes y después del tránsito, y el monto descontado.
 * Propietario es Expert de esta información.
 */
public class ResultadoTransito {
    private final Transito transito;
    private final double saldoAntes;
    private final double saldoDespues;
    private final double montoDescontado;

    public ResultadoTransito(Transito transito, double saldoAntes, double saldoDespues, double montoDescontado) {
        this.transito = transito;
        this.saldoAntes = saldoAntes;
        this.saldoDespues = saldoDespues;
        this.montoDescontado = montoDescontado;
    }

    public Transito getTransito() {
        return transito;
    }

    public double getSaldoAntes() {
        return saldoAntes;
    }

    public double getSaldoDespues() {
        return saldoDespues;
    }

    public double getMontoDescontado() {
        return montoDescontado;
    }
}
