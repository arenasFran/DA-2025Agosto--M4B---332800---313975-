package obligatorio.obli.models;

import java.util.List;

public class Puesto {
    private String nombre;
    private String direccion;
    private List<Tarifa> tarifas;

    public Puesto(String nombre, String direccion, List<Tarifa> tarifas) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tarifas = tarifas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Tarifa> getTarifas() {
        return tarifas;
    }

    public void setTarifas(List<Tarifa> tarifas) {
        this.tarifas = tarifas;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Puesto puesto = (Puesto) obj;
        return this.nombre.equals(puesto.getNombre());
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    public Tarifa obtenerTarifaPorCategoria(String categoriaVehiculo) {
        if (categoriaVehiculo == null || tarifas == null) {
            return null;
        }
        for (Tarifa t : tarifas) {
            if (t.esParaCategoria(categoriaVehiculo)) {
                return t;
            }
        }
        return null;
    }

    public boolean tieneTarifaParaCategoria(String categoriaVehiculo) {
        return obtenerTarifaPorCategoria(categoriaVehiculo) != null;
    }
}
