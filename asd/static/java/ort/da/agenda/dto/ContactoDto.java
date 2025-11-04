/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.dto;

import ort.da.agenda.modelo.Contacto;

/**
 *
 * @author PC
 */
public class ContactoDto {
    
    private String nombre;
    private String numero;
    private String tipoContacto;
    
    
    public ContactoDto(Contacto contacto) {
        
        nombre = contacto.getNombre();
        numero = contacto.getTelefono().getNumero();
        tipoContacto = contacto.getTipoContacto().getNombre();
       
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }

    public String getTipoContacto() {
        return tipoContacto;
    }
    
    
}
