package obligatorio.obli.models.Sistemas;

import java.util.List;
import obligatorio.obli.models.Vehiculo;

public class SistemaVehiculo {
    private List<Vehiculo> vehiculos;

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public Vehiculo getVehiculoPorMatricula(String matricula) {
        for (Vehiculo v : vehiculos) {
            if (v.getMatricula() == matricula) {
                return v;
            }
        }
        return null;
    }
}
