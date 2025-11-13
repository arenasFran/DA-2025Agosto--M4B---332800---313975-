package obligatorio.obli.models.Bonificaciones;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import obligatorio.obli.models.Transito;

public class BonificacionFrecuentes extends Bonificacion {

    public BonificacionFrecuentes() {
        super("Frecuentes");
    }

    @Override
    public double calcularDescuento(Transito transito, List<Transito> transitosDelDia) {

        int transitosPreviosHoy = contarTransitosPreviosHoy(transito, transitosDelDia);

        if (transitosPreviosHoy == 0) {
            return 0.0;
        }

        return 0.5;
    }

    private int contarTransitosPreviosHoy(Transito transitoActual, List<Transito> transitosDelDia) {
        if (transitosDelDia == null) {
            return 0;
        }

        int contador = 0;
        Date fechaActual = transitoActual.getFecha();

        for (Transito t : transitosDelDia) {

            if (esMismoDia(t.getFecha(), fechaActual)
                    && t.getVehiculo().equals(transitoActual.getVehiculo())
                    && t.getPuesto().equals(transitoActual.getPuesto())
                    && t.getFecha().before(fechaActual)) { // Solo contar los anteriores
                contador++;
            }
        }

        return contador;
    }

    private boolean esMismoDia(Date fecha1, Date fecha2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
