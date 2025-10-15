package obligatorio.obli.models.Sistemas;

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

    public User Login(String ci, String password) {
        SistemaLogin sistemaLogin = SistemaLogin.getInstancia();
        return sistemaLogin.login(ci, password);
    }
}
