package obligatorio.obli.models.Sistemas;

import java.util.List;
import java.util.ArrayList;

import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.Precarga;

public class SistemaUsuario {

    private static SistemaUsuario instancia;

    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Administrador> administradores = new ArrayList<>();

    private SistemaUsuario() {
        cargarDatos();
    }

    public static SistemaUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SistemaUsuario();
        }
        return instancia;
    }

    private void cargarDatos() {
        propietarios.addAll(Precarga.cargarPropietarios());
        administradores.addAll(Precarga.cargarAdministradores());
    }

    public List<Propietario> getPropietarios() {
        return new ArrayList<>(propietarios);
    }

    public List<Administrador> getAdministradores() {
        return new ArrayList<>(administradores);
    }

    public void agregarPropietario(Propietario p) {
        propietarios.add(p);
    }

    public void agregarAdministrador(Administrador a) {
        administradores.add(a);
    }
}
