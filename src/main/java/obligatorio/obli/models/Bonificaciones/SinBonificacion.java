package obligatorio.obli.models.Bonificaciones;

import java.util.List;

import obligatorio.obli.models.Transito;

public class SinBonificacion extends Bonificacion {

    public SinBonificacion() {
        super("Sin Bonificación");
    }

    @Override
    public double calcularDescuento(Transito transito, List<Transito> transitosDelDia) {
        return 0.0;
    }
}
