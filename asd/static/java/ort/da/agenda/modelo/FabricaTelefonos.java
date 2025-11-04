package ort.da.agenda.modelo;

public class FabricaTelefonos {

     //Metodo fabrica (Factoy Method)
    public static Telefono crearTelefono(String numero, TipoTelefono tipoTelefono) {
       if(tipoTelefono.getNombre().equals("Fijo")) return new Fijo(numero, tipoTelefono);
       if(tipoTelefono.getNombre().equals("Celular")) return new Celular(numero, tipoTelefono);
       if(tipoTelefono.getNombre().equals("Internacional")) return new Internacional(numero, tipoTelefono);
       return null;
    }

}
