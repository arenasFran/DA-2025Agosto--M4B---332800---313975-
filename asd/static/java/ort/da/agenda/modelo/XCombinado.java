package ort.da.agenda.modelo;

public class XCombinado extends CriterioBusqueda{

    public XCombinado() {
        super("Combinado");
        
    }

    @Override
    public boolean filtrar(Contacto c, String filtro) {
        return c.getNombre().contains(filtro) || c.getTelefono().getNumero().contains(filtro);
    }

}
