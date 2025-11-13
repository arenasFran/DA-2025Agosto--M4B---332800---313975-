package obligatorio.obli.models.Bonificaciones;

import java.util.Calendar;
import java.util.List;

import obligatorio.obli.models.Transito;

public class BonificacionTrabajadores extends Bonificacion {

    public BonificacionTrabajadores() {
        super("Trabajadores");
    }

    @Override
    public double calcularDescuento(Transito transito, List<Transito> transitosDelDia) {

        if (esDiaDeSemana(transito.getFecha())) {
            return 0.8;
        }

        return 0.0;
    }

    /**
     * Verifica si una fecha corresponde a un día de semana (lunes a viernes)
     */
    private boolean esDiaDeSemana(java.util.Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);

        // Calendar.MONDAY = 2, Calendar.FRIDAY = 6
        return diaSemana >= Calendar.MONDAY && diaSemana <= Calendar.FRIDAY;
    }
}
