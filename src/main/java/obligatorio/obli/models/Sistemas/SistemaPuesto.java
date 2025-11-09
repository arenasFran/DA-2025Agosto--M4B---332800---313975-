package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import java.util.List;

import obligatorio.obli.Precarga;
import obligatorio.obli.exceptions.puesto.PuestoNoEncontradoException;
import obligatorio.obli.models.Puesto;

public class SistemaPuesto {

    private static SistemaPuesto instancia;
    private final List<Puesto> puestos = new ArrayList<>();

    private SistemaPuesto() {
        cargarDatos();
    }

    public static SistemaPuesto getInstancia() {
        if (instancia == null) {
            instancia = new SistemaPuesto();
        }
        return instancia;
    }

    private void cargarDatos() {
        puestos.addAll(Precarga.cargarPuestos());
    }

    public List<Puesto> getPuestos() {
        return new ArrayList<>(puestos);
    }

    public Puesto buscarPorNombre(String nombre) throws PuestoNoEncontradoException {
        for (Puesto puesto : puestos) {
            if (puesto.getNombre().equals(nombre)) {
                return puesto;
            }
        }

        throw new PuestoNoEncontradoException(nombre);
    }
}
