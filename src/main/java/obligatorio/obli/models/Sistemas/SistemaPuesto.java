package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.Precarga;
import obligatorio.obli.exceptions.PuestoException;
import obligatorio.obli.models.Puesto;

public class SistemaPuesto {
    private final List<Puesto> puestos = new ArrayList<>();

    public SistemaPuesto() {
        cargarDatos();
    }

    private void cargarDatos() {
        puestos.addAll(Precarga.cargarPuestos());
    }

    public List<Puesto> getPuestos() {
        return new ArrayList<>(puestos);
    }

    public Puesto buscarPorNombre(String nombre) throws PuestoException {
        for (Puesto puesto : puestos) {
            if (puesto.getNombre().equals(nombre)) {
                return puesto;
            }
        }

        throw new PuestoException(String.format("No se encontró ningún puesto con el nombre: %s.", nombre));
    }
}
