package obligatorio.obli.models.Sistemas;

import java.util.ArrayList;
import obligatorio.obli.models.Usuarios.User;

public class SistemaLogin {

    private static SistemaLogin instancia;

    private ArrayList<User> usuarios = new ArrayList<>();

    private SistemaLogin() {

    }

    public static SistemaLogin getInstancia() {
        if (instancia == null) {
            instancia = new SistemaLogin();
        }
        return instancia;
    }

    public User login(String ci, String password) {
        for (User user : usuarios) {
            if (user.getCi().equals(ci) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new RuntimeException("Usuario o contraseña incorrectos");
    }

    public void agregarUsuario(User user) {
        usuarios.add(user);
    }

}
