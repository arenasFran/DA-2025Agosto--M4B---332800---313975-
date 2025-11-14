package obligatorio.obli.dtos;

import obligatorio.obli.models.Tarifa;

public class TarifaDTO {
    private String nombreCategoria;
    private double monto;

    public TarifaDTO(Tarifa tarifa) {
        this.nombreCategoria = tarifa.getCategoriaVehiculo();
        this.monto = tarifa.getMonto();
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
