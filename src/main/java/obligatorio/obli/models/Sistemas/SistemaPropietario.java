package obligatorio.obli.models.Sistemas;

import java.util.List;

import obligatorio.obli.models.Propietario;

public class SistemaPropietario {
    public List<Propietario> propietarios;

    public Propietario getPropietarioPorCi(String Ci) {
        for (Propietario p : propietarios) {
            if (p.getCi() == Ci) {
                return p;
            }
        }
        return null;
    }
}
