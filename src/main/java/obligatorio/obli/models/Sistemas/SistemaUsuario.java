package obligatorio.obli.models.Sistemas;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.Precarga;

@Service
public class SistemaUsuario {
    private List<Propietario> propietarios;
    private List<Administrador> administradores;

    public SistemaUsuario() {
        this.propietarios = new ArrayList<>();
        this.administradores = new ArrayList<>();
        cargarDatos();
    }

    private void cargarDatos() {
        List<Propietario> propietarios = Precarga.cargarPropietarios();
        for (Propietario p : propietarios) {
            this.propietarios.add(p);
        }
        
        List<Administrador> administradores = Precarga.cargarAdministradores();
        for (Administrador a : administradores) {
            this.administradores.add(a);
        }
    }

    public List<Propietario> getPropietarios() {
        return propietarios;
    }

    public List<Administrador> getAdministradores() {
        return administradores;
    }
}
