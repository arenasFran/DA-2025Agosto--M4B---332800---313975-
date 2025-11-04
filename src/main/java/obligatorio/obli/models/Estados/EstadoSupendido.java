package obligatorio.obli.models.Estados;

public class EstadoSupendido extends Estado {

    public EstadoSupendido(String nombre) {
        super("Suspendido");
    }

    @Override
    public boolean puedeIniciarSesion() {
        return true;
    }

    @Override
    public boolean puedeRealizarTransito() {
        return false;
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
        return true;
    }

    @Override
    public String getMensajeRestriccion() {
        return "Usuario suspendido. No puede realizar tránsitos.";
    }

}
