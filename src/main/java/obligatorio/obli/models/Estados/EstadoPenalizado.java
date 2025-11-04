package obligatorio.obli.models.Estados;

public class EstadoPenalizado extends Estado {

    public EstadoPenalizado(String nombre) {
        super("Penalizado");
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
        return false;
    }

    @Override
    public boolean recibeNotificaciones() {
        return false;
    }

    @Override
    public String getMensajeRestriccion() {
        return "Usuario penalizado. No se registran notificaciones y las bonificaciones no aplican en tránsitos.";
    }

}
