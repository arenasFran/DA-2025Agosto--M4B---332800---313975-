package obligatorio.obli.models;

import java.util.Date;
import java.util.List;

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

    /**
     * Calcula el monto final del tránsito aplicando la bonificación
     * 
     * @param transitosDelDia Lista de tránsitos realizados en el mismo día
     *                        (necesario para bonificaciones frecuentes)
     * @return Monto final a pagar después de aplicar descuentos
     */
    public double calcularMontoFinal(List<Transito> transitosDelDia) {
        if (bono == null) {
            return tarifa.getMonto();
        }

        double descuento = bono.calcularDescuento(this, transitosDelDia);
        double montoBase = tarifa.getMonto();

        return montoBase * (1.0 - descuento);
    }

    /**
     * Calcula el monto final sin considerar otros tránsitos del día
     * Útil cuando no se tiene el historial completo
     * 
     * @return Monto final a pagar
     */
    public double calcularMontoFinal() {
        return calcularMontoFinal(null);
    }

    /**
     * Obtiene el porcentaje de descuento aplicado
     * 
     * @param transitosDelDia Lista de tránsitos del día
     * @return Porcentaje de descuento (0.0 a 1.0)
     */
    public double obtenerPorcentajeDescuento(List<Transito> transitosDelDia) {
        if (bono == null) {
            return 0.0;
        }
        return bono.calcularDescuento(this, transitosDelDia);
    }
}
