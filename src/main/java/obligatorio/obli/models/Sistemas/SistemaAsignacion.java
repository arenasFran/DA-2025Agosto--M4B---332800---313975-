package obligatorio.obli.models.Sistemas;

import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Bonificacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Usuarios.Propietario;

public class SistemaAsignacion {

    private static SistemaAsignacion instancia;

    private SistemaAsignacion() {

    }

    public static SistemaAsignacion getInstancia() {
        if (instancia == null) {
            instancia = new SistemaAsignacion();
        }
        return instancia;
    }

    public Asignacion asignarBonificacion(Propietario propietario, Bonificacion bonificacion, Puesto puesto) {
        Asignacion asignacion = new Asignacion(bonificacion, puesto);
        propietario.agregarAsignacion(asignacion);
        return asignacion;
    }

}
