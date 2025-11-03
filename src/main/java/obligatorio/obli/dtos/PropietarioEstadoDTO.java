package obligatorio.obli.dtos;

/**
 * Data Transfer Object for Propietario estado information.
 * Used in the cambiar-estado use case to communicate with the frontend.
 */
public class PropietarioEstadoDTO {
    private String cedula;
    private String nombre;
    private String estado;

    public PropietarioEstadoDTO(String cedula, String nombre, String estado) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.estado = estado;
    }

    // Getters and setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
