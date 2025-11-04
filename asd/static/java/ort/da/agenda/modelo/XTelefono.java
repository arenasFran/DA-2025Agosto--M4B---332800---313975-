package ort.da.agenda.modelo;

public class XTelefono extends CriterioBusqueda{

    public XTelefono() {
        super("Por telefono");
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean filtrar(Contacto c, String filtro) {
      return c.getTelefono().getNumero().contains(filtro);
    }

}
