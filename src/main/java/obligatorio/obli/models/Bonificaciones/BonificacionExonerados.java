package obligatorio.obli.models.Bonificaciones;

import java.util.List;

import obligatorio.obli.models.Transito;

public class BonificacionExonerados extends Bonificacion {

    public BonificacionExonerados() {
        super("Exonerados");
    }

    @Override
    public double calcularDescuento(Transito transito, List<Transito> transitosDelDia) {
        // Los exonerados no pagan (100% de descuento)
        return 1.0;
    }
}
