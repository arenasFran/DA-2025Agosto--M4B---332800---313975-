package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.Precarga;
import obligatorio.obli.exceptions.BonificacionException;
import obligatorio.obli.models.Bonificaciones.Bonificacion;

public class SistemaBonificacion {
    private final List<Bonificacion> bonificaciones = new ArrayList<>();

    public SistemaBonificacion() {
        cargarDatos();
    }

    private void cargarDatos() {
        bonificaciones.addAll(Precarga.cargarBonificaciones());
    }

    public List<Bonificacion> getBonificacions() {
        return new ArrayList<>(bonificaciones);
    }

    public Bonificacion buscarPorNombre(String nombre) throws BonificacionException {
        for (Bonificacion bonificacion : bonificaciones) {
            if (bonificacion.getNombre().equals(nombre)) {
                return bonificacion;
            }
        }

        throw new BonificacionException(String.format("No se encontró ninguna bonificación con el nombre: %s.", nombre));
    }
}
