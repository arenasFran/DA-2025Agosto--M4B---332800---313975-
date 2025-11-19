package obligatorio.obli.models.Sistemas;

import java.util.List;
import java.util.ArrayList;

import obligatorio.obli.models.Vehiculo;
import obligatorio.obli.models.Usuarios.Administrador;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.Precarga;
import obligatorio.obli.exceptions.PropietarioException;
import obligatorio.obli.exceptions.AdministradorException;

public class SistemaUsuario {
    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Administrador> administradores = new ArrayList<>();

    public SistemaUsuario() {
        cargarDatos();
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

    public Propietario getPropietarioPorCi(String ci) throws PropietarioException {
        for (Propietario p : propietarios) {
            if (p.getCi().equals(ci)) {
                return p;
            }
        }

        throw new PropietarioException("No se encontró ningún propietario con la cédula: " + ci);
    }

    public Administrador getAdministradorPorCi(String ci) throws AdministradorException {
        for (Administrador a : administradores) {
            if (a.getCi().equals(ci)) {
                return a;
            }
        }

        throw new AdministradorException("No se encontró ningún administrador con la cédula: " + ci);
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) throws Exception {
        for (Propietario prop : propietarios) {
            for (Vehiculo v : prop.getVehiculo()) {
                if (v.getMatricula().equalsIgnoreCase(matricula)) {
                    return v;
                }
            }
        }
        throw new Exception("No existe el vehículo");
    }

    public Propietario buscarPropietarioDeVehiculo(Vehiculo vehiculo) throws Exception {
        for (Propietario prop : propietarios) {
            if (prop.tieneVehiculo(vehiculo)) {
                return prop;
            }
        }
        throw new Exception("El vehículo no tiene propietario registrado");
    }

}
