package obligatorio.obli.models.Estados;

public class EstadoHabilitado extends Estado {

    public EstadoHabilitado(String nombre) {
        super("Habilitado");
    }

    @Override
    public boolean puedeIniciarSesion() {
        return true;
    }

    @Override
    public boolean puedeRealizarTransito() {
        return true;
    }

    @Override
    public boolean puedeRecibirBonificacion() {
        return true;
    }

    @Override
    public boolean aplicanBonificacionesEnTransito() {
        return true;
    }

    @Override
    public boolean recibeNotificaciones() {
        return true;
    }

    @Override
    public String getMensajeRestriccion() {
        return "";
    }

}
