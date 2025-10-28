package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Bonificacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Usuarios.Propietario;

public class SistemaAsignacion {

    private static SistemaAsignacion instancia;
    private List<Asignacion> asignaciones = new ArrayList<>();

    private SistemaAsignacion() {

    }

    public static SistemaAsignacion getInstancia() {
        if (instancia == null) {
            instancia = new SistemaAsignacion();
        }
        return instancia;
    }

    public void asignarBonificacion() {

    }

    public Asignacion asignarBonificacion(Propietario propietario, Bonificacion bonificacion, Puesto puesto) {
        Asignacion asignacion = new Asignacion(propietario, bonificacion, puesto);
        asignaciones.add(asignacion);
        return asignacion;
    }

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public List<Asignacion> getAsignacionesPorPropietario(String ci) {
        List<Asignacion> resultado = new ArrayList<>();
        for (Asignacion a : asignaciones) {
            if (a.propietario != null && a.propietario.getCi().equals(ci)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

}
