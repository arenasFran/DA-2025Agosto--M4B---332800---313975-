/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

/**
 *
 * @author PC
 */
public class Contacto {
    
    private String nombre;
    private Telefono telefono;
    private TipoContacto tipoContacto;

    public Contacto(String nombre, String numero, TipoContacto tipoContacto,TipoTelefono tipoTelefono) {
        this.nombre = nombre;
        this.telefono = FabricaTelefonos.crearTelefono(numero, tipoTelefono);
        this.tipoContacto = tipoContacto;
    }

       public String getNombre() {
        return nombre;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public TipoContacto getTipoContacto() {
        return tipoContacto;
    }

    @Override
    public String toString() {
        return nombre + " (" + telefono.getNumero() + ")";
    }
    public boolean equals(Object o){
        Contacto c = (Contacto)o;
        return nombre.equalsIgnoreCase(c.nombre) &&
               telefono.getNumero().equals(c.telefono.getNumero());
    }
    
    public void validar() throws AgendaException{
        
        if(tipoContacto == null) throw new AgendaException("Falta el tipo de contacto");
        if(nombre == null || nombre.trim().isEmpty()) throw new AgendaException("Falta el nombre del contacto");
        if(telefono == null) throw new AgendaException("Falta el telefono del contacto");
        telefono.validar();
        ///TODO LLAMAR A VALIDAR DE TELEFONO
    }
}
