package obligatorio.obli.models;

/**
 * Clase que encapsula el resultado de un tránsito emulado
 * incluyendo el saldo antes y después del tránsito.
 * Propietario es Expert de esta información.
 */
public class ResultadoTransito {
    private final Transito transito;
    private final double saldoAntes;
    private final double saldoDespues;

    public ResultadoTransito(Transito transito, double saldoAntes, double saldoDespues) {
        this.transito = transito;
        this.saldoAntes = saldoAntes;
        this.saldoDespues = saldoDespues;
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
}
