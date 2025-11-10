package obligatorio.obli.models;

import java.util.Date;

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
}
