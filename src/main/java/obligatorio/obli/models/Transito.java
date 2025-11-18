package obligatorio.obli.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

import obligatorio.obli.models.Bonificaciones.Bonificacion;
import obligatorio.obli.models.Usuarios.Propietario;

public class Transito {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final int DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final Locale LOCALE_URUGUAY = new Locale("es", "UY");
    private static final DecimalFormatSymbols DECIMAL_SYMBOLS;

    static {
        DECIMAL_SYMBOLS = new DecimalFormatSymbols(LOCALE_URUGUAY);
        DECIMAL_SYMBOLS.setDecimalSeparator(',');
        DECIMAL_SYMBOLS.setGroupingSeparator('.');
    }

    private Puesto puesto;
    private Vehiculo vehiculo;
    private Tarifa tarifa;
    private Date fecha;
    private Bonificacion bono;

    public Transito(Puesto puesto, Vehiculo vehiculo, Tarifa tarifa, Date fecha, Bonificacion bono) {
        this.puesto = puesto;
        this.vehiculo = vehiculo;
        this.tarifa = tarifa;
        this.fecha = fecha;
        this.bono = bono;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Bonificacion getBono() {
        return bono;
    }

    public void setBono(Bonificacion bono) {
        this.bono = bono;
    }

    public double calcularMontoFinal(List<Transito> transitosDelDia) {
        double montoBase = tarifa.getMonto();

        if (bono == null) {
            return redondearMonto(montoBase);
        }

        double descuento = bono.calcularDescuento(this, transitosDelDia);
        double montoConDescuento = montoBase * (1.0 - descuento);

        // Asegurar que el monto nunca sea negativo y redondear a 2 decimales
        double montoFinal = Math.max(0.0, montoConDescuento);
        return redondearMonto(montoFinal);
    }

    private double redondearMonto(double monto) {
        BigDecimal bd = BigDecimal.valueOf(monto);
        bd = bd.setScale(DECIMAL_PLACES, ROUNDING_MODE);
        return bd.doubleValue();
    }

    public double calcularMontoFinal() {
        return calcularMontoFinal(null);
    }

    public double obtenerPorcentajeDescuento(List<Transito> transitosDelDia) {
        if (bono == null) {
            return 0.0;
        }
        return bono.calcularDescuento(this, transitosDelDia);
    }

    public double obtenerMontoTarifaBase() {
        return tarifa.getMonto();
    }

    public double obtenerMontoBonificacion(List<Transito> transitosDelDia) {
        double montoBase = obtenerMontoTarifaBase();
        double montoFinal = calcularMontoFinal(transitosDelDia);
        double descuento = montoBase - montoFinal;
        return Math.max(0.0, redondearMonto(descuento));
    }

    public double obtenerMontoPagado(List<Transito> transitosDelDia) {
        return calcularMontoFinal(transitosDelDia);
    }

    public String obtenerFechaFormateada() {
        return sdf.format(fecha);
    }

    public String obtenerMontoTarifaFormateado() {
        return formatearMoneda(obtenerMontoTarifaBase());
    }

    public String obtenerMontoBonificacionFormateado(Propietario propietario) {
        List<Transito> transitosDelDia = propietario.getTransitos();
        double montoBonificacionAplicada = obtenerMontoBonificacion(transitosDelDia);
        if (montoBonificacionAplicada > 0) {
            return formatearMoneda(-montoBonificacionAplicada);
        }
        return formatearMoneda(0);
    }

    public String obtenerMontoPagadoFormateado(Propietario propietario) {
        List<Transito> transitosDelDia = propietario.getTransitos();
        double montoPagado = obtenerMontoPagado(transitosDelDia);
        return formatearMoneda(montoPagado);
    }

    private String formatearMoneda(double monto) {
        DecimalFormat decimalFormat = new DecimalFormat("$ ###,##0.00", DECIMAL_SYMBOLS);
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        return decimalFormat.format(monto);
    }

    public static Transito crearConFechaString(Puesto puesto, Vehiculo vehiculo, Tarifa tarifa,
            String fechaString, Bonificacion bono) throws Exception {
        Date fecha = sdf.parse(fechaString);
        return new Transito(puesto, vehiculo, tarifa, fecha, bono);
    }
}
