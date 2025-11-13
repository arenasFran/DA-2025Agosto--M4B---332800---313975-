package obligatorio.obli.models;

import obligatorio.obli.models.Categorias.Categoria;

public class Vehiculo {
    private String matricula;
    private String modelo;
    private String color;
    private Categoria categoria;
    // agregar propietario

    public Vehiculo(String matricula, String modelo, String color, Categoria categoria) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.color = color;
        this.categoria = categoria;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Vehiculo vehiculo = (Vehiculo) obj;
        return this.matricula.equals(vehiculo.getMatricula());
    }

    @Override
    public int hashCode() {
        return matricula.hashCode();
    }

    public String getNombreCategoria() {
        if (categoria == null) {
            return null;
        }
        return categoria.getNombre();
    }
}
