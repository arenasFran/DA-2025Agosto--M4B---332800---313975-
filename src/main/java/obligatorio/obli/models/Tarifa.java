package obligatorio.obli.models;

public class Tarifa {
    private double monto;
    private String categoriaVehiculo;

    public Tarifa(double monto, String categoriaVehiculo) {
        this.monto = monto;
        this.categoriaVehiculo = categoriaVehiculo;
    }

    // Getters and setters
    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getCategoriaVehiculo() {
        return categoriaVehiculo;
    }

    public void setCategoriaVehiculo(String categoriaVehiculo) {
        this.categoriaVehiculo = categoriaVehiculo;
    }
}
