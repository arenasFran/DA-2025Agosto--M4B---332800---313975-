package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.Precarga;
import obligatorio.obli.exceptions.bonificaciones.BonificacionNoEncontradaException;
import obligatorio.obli.models.Bonificaciones.Bonificacion;

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

    public Bonificacion buscarPorNombre(String nombre) throws BonificacionNoEncontradaException {
        for (Bonificacion bonificacion : bonificaciones) {
            if (bonificacion.getNombre().equals(nombre)) {
                return bonificacion;
            }
        }

        throw new BonificacionNoEncontradaException(nombre);
    }
}
