package obligatorio.obli.models.Sistemas;

import java.util.List;
import java.util.ArrayList;

import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.Precarga;
import obligatorio.obli.exceptions.administrador.AdministradorNoEncontradoException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;

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

    public Propietario getPropietarioPorCi(String ci) throws PropietarioNoEncontradoException {
        for (Propietario p : propietarios) {
            if (p.getCi().equals(ci)) {
                return p;
            }
        }

        throw new PropietarioNoEncontradoException(ci);
    }

    public Administrador getAdministradorPorCi(String ci) throws AdministradorNoEncontradoException {
        for (Administrador a : administradores) {
            if (a.getCi().equals(ci)) {
                return a;
            }
        }

        throw new AdministradorNoEncontradoException(ci);
    }

    /**
     * Busca un vehículo por su matrícula entre todos los propietarios
     * 
     * @return El vehículo encontrado
     * @throws Exception si no se encuentra
     */
    public obligatorio.obli.models.Vehiculo buscarVehiculoPorMatricula(String matricula) throws Exception {
        for (Propietario prop : propietarios) {
            for (obligatorio.obli.models.Vehiculo v : prop.getVehiculo()) {
                if (v.getMatricula().equalsIgnoreCase(matricula)) {
                    return v;
                }
            }
        }
        throw new Exception("No existe el vehículo");
    }

    /**
     * Busca el propietario de un vehículo específico
     * 
     * @return El propietario dueño del vehículo
     * @throws Exception si no se encuentra
     */
    public Propietario buscarPropietarioDeVehiculo(obligatorio.obli.models.Vehiculo vehiculo) throws Exception {
        for (Propietario prop : propietarios) {
            if (prop.tieneVehiculo(vehiculo)) {
                return prop;
            }
        }
        throw new Exception("El vehículo no tiene propietario registrado");
    }

}
