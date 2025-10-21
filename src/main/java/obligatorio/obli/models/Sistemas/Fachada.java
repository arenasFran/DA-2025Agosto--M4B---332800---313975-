package obligatorio.obli.models.Sistemas;

import java.util.List;

import obligatorio.obli.models.Bonificacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.models.Usuarios.User;

public class Fachada {

    private static Fachada instancia;

    private Fachada() {

    }

    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    public User loginPropietario(String ci, String password) {
        SistemaLogin sistemaLogin = SistemaLogin.getInstancia();
        return sistemaLogin.loginPropietario(ci, password);
    }

    public User loginAdmin(String ci, String password) {
        SistemaLogin sistemaLogin = SistemaLogin.getInstancia();
        return sistemaLogin.loginAdmin(ci, password);
    }

    public Propietario buscarPropietarioPorCi(String ci) {
        SistemaUsuario s = SistemaUsuario.getInstancia();
        return s.devolverPorpietarioPorCi(ci);
    }

    public List<Bonificacion> mostrarBonificaciones() {
        SistemaBonificacion s = SistemaBonificacion.getInstancia();
        return s.getBonificacions();
    }

    public List<Puesto> getPuestos() {
        SistemaPuesto s = SistemaPuesto.getInstancia();
        return s.getPuestos();
    }

    public void asignarBonificacion(String ci, String nombreBonificacion, String nombrePuesto) {
        Propietario p = SistemaUsuario.getInstancia().devolverPorpietarioPorCi(ci);
        if (p == null)
            throw new RuntimeException("Propietario no encontrado");

        Bonificacion bonificacion = SistemaBonificacion.getInstancia().buscarPorNombre(nombreBonificacion);
        if (bonificacion == null)
            throw new RuntimeException("Bonificación no encontrada");

        Puesto puesto = SistemaPuesto.getInstancia().buscarPorNombre(nombrePuesto);
        if (puesto == null)
            throw new RuntimeException("Puesto no encontrado");

        SistemaAsignacion.getInstancia().asignarBonificacion(p, bonificacion, puesto);

    }
}
