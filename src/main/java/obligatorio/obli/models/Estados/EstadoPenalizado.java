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
        return false;
    }

    @Override
    public boolean aplicanBonificacionesEnTransito() {
        return false;
    }

    @Override
    public boolean recibeNotificaciones() {
        return false;
    }
}
