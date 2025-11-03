package obligatorio.obli.models.Estados;

public abstract class Estado {
    private String nombre;

    public Estado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static Estado fromNombre(String nombre) throws IllegalArgumentException {
        switch (nombre) {
            case "Habilitado":
                return new EstadoHabilitado("Habilitado");
            case "Deshabilitado":
                return new EstadoDeshabilitado("Deshabilitado");
            case "Suspendido":
                return new EstadoSupendido("Suspendido");
            case "Penalizado":
                return new EstadoPenalizado("Penalizado");
            default:
                throw new IllegalArgumentException("Estado no válido: " + nombre);
        }
    }
}
