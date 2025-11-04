package ort.da.agenda.modelo;

public class XNonbre extends CriterioBusqueda{

    
    public XNonbre() {
        super("Por nombre");
        
    }

    @Override
    public boolean filtrar(Contacto c, String filtro) {
       return c.getNombre().startsWith(filtro);
    }

   

}
