package ort.da.agenda.modelo;

public class Internacional extends Telefono{

    public Internacional(String numero, TipoTelefono tipoTelefono) {
        super(numero, tipoTelefono);
     
    }

    @Override
    public void validarNumero() throws AgendaException {
        if(getTipoTelefono()==null) throw new AgendaException("Falta el tipo de telefono");
         int desde = -1;
        if(getNumero().startsWith("+")) desde = 1;
        else if(getNumero().startsWith("00")) desde = 2;
        if(desde==-1) throw new AgendaException("El numero debe comenzar con '+' o con '00'");
        char c;
        int digitos=0;
        for(int x=desde;x<getNumero().length();x++){
            c=getNumero().charAt(x);
           if(c!=' '  && !Character.isDigit(c)) throw new AgendaException("Solo puede ingresar numeros o espacios");
            if(Character.isDigit(c)) digitos++; 
        }
        if(digitos<10) throw new AgendaException("Debe ingresar al menos 10 digitos");

    }

}
