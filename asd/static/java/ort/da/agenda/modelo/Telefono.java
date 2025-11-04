package ort.da.agenda.modelo;

public abstract class Telefono {

    private String numero;
    private TipoTelefono tipoTelefono;

    public Telefono(String numero, TipoTelefono tipoTelefono) {
        this.numero = numero;
        this.tipoTelefono = tipoTelefono;
    }

    public String getNumero() {
        return numero;
    }

    public TipoTelefono getTipoTelefono() {
        return tipoTelefono;
    }
    public boolean esNumero(String numero) {
        try{
            Long.parseLong(numero);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    //Metodo template, tiene algunos pasos que se van a implementar en las subclases
    public void validar() throws AgendaException{
        if(getTipoTelefono()==null) throw new AgendaException("Falta el tipo de telefono");
        validarNumero(); //se implementa en las sublcases
    }
    public abstract void validarNumero() throws AgendaException;

}
