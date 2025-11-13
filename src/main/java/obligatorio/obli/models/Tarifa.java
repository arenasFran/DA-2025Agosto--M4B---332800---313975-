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

    /** Verifica si esta tarifa es para una categoría específica de vehículo */
    public boolean esParaCategoria(String categoria) {
        if (categoria == null || this.categoriaVehiculo == null) {
            return false;
        }
        return this.categoriaVehiculo.equalsIgnoreCase(categoria);
    }

    /** Calcula el monto con un descuento aplicado */
    public double calcularMontoConDescuento(double porcentajeDescuento) {
        if (porcentajeDescuento < 0.0 || porcentajeDescuento > 1.0) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0.0 y 1.0");
        }
        return this.monto * (1.0 - porcentajeDescuento);
    }
}
