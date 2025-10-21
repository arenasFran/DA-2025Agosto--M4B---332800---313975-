package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.Precarga;
import obligatorio.obli.models.Bonificacion;

public class SistemaBonificacion {

    private static SistemaBonificacion instancia;
    private final List<Bonificacion> bonificaciones = new ArrayList<>();

    private SistemaBonificacion() {
        cargarDatos();
    }

    public static SistemaBonificacion getInstancia() {
        if (instancia == null) {
            instancia = new SistemaBonificacion();
        }
        return instancia;
    }

    private void cargarDatos() {
        bonificaciones.addAll(Precarga.cargarBonificaciones());
    }

    public List<Bonificacion> getBonificacions() {
        return new ArrayList<>(bonificaciones);
    }

    public Bonificacion buscarPorNombre(String nombre) {
        Bonificacion b;

        for (Bonificacion bonificacion : bonificaciones) {
            if (bonificacion.getNombre().equals(nombre)) {
                b = bonificacion;
                return b;
            }
        }
        return null;
    }
}
