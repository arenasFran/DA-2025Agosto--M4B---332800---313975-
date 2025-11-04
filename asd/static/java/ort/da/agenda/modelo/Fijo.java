package ort.da.agenda.modelo;

public class Fijo extends Telefono{

    public Fijo(String numero, TipoTelefono tipoTelefono) {
        super(numero, tipoTelefono);
    }

    @Override
    public void validarNumero() throws AgendaException {
        if(getTipoTelefono()==null) throw new AgendaException("Falta el tipo de telefono");
        if(!esNumero(getNumero())) throw new AgendaException("El telefono debe ser un numero");
        if(getNumero().length()!=8)  throw new AgendaException("El telfono fijo debe tener 8 digitos");
    }
    

}
