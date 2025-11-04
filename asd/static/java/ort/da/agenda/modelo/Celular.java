package ort.da.agenda.modelo;

public class Celular extends Telefono{

    public Celular(String numero, TipoTelefono tipoTelefono) {
        super(numero, tipoTelefono);
        
    }

    @Override
    public void validarNumero() throws AgendaException {
     if(getTipoTelefono()==null) throw new AgendaException("Falta el tipo de telefono");
         if(!esNumero(getNumero())) throw new AgendaException("El telefono debe ser un numero");
         if(getNumero().length()!=9)  throw new AgendaException("El telfono celular debe tener 9 digitos");
         if(!getNumero().startsWith("09")) throw new AgendaException("El telefono celular debe comenzar con 09");
    }
    

}
