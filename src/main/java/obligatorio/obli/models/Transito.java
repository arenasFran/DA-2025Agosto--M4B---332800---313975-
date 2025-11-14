package obligatorio.obli.models;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import obligatorio.obli.models.Bonificaciones.Bonificacion;

public class Transito {
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

    // Getters and setters
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
        if (bono == null) {
            return tarifa.getMonto();
        }

        double descuento = bono.calcularDescuento(this, transitosDelDia);
        double montoBase = tarifa.getMonto();

        return montoBase * (1.0 - descuento);
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

    /**
     * Transito es Expert: sabe cómo formatear su fecha
     */
    public String obtenerFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return sdf.format(fecha);
    }

    /**
     * Factory method: Crea un Transito parseando la fecha desde String.
     * Transito es Expert porque sabe qué formato de fecha espera.
     */
    public static Transito crearConFechaString(Puesto puesto, Vehiculo vehiculo, Tarifa tarifa,
            String fechaString, Bonificacion bono) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date fecha = sdf.parse(fechaString);
        return new Transito(puesto, vehiculo, tarifa, fecha, bono);
    }
}
