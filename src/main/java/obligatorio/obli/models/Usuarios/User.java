package obligatorio.obli.models.Usuarios;

import obligatorio.obli.observador.Observable;

public abstract class User extends Observable {
    private int id;
    private String Ci;
    private String nombre;
    private String password;

    public User(int id, String ci, String nombre, String password) {
        this.id = id;
        this.Ci = ci;
        this.nombre = nombre;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCi() {
        return Ci;
    }

    public void setCi(String ci) {
        Ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
