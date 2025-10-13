package obligatorio.obli.models;

public class Vehiculo {
    private String matricula;
    private String modelo;
    private String color;
    private Categoria categoria;

    public Vehiculo(String matricula, String modelo, String color, Categoria categoria) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.color = color;
        this.categoria = categoria;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public String getColor() {
        return color;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
