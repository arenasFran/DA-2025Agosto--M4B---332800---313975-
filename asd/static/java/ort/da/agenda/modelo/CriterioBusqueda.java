package ort.da.agenda.modelo;

import java.util.List;

public abstract class CriterioBusqueda {

    private String nombre;

    public CriterioBusqueda(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    public abstract boolean filtrar(Contacto c,String filtro);
}
