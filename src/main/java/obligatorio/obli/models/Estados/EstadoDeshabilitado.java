package obligatorio.obli.models.Estados;

public class EstadoDeshabilitado extends Estado {

    public EstadoDeshabilitado(String nombre) {
        super("Deshabilitado");
    }

    @Override
    public boolean puedeIniciarSesion() {
        return false;
    }

    @Override
    public boolean puedeRealizarTransito() {
        return false;
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
