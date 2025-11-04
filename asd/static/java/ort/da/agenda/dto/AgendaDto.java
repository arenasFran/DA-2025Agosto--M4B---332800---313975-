/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.dto;

import java.util.ArrayList;
import java.util.List;

import ort.da.agenda.modelo.Agenda;
import ort.da.agenda.modelo.Contacto;

/**
 *
 * @author PC
 */
public class AgendaDto {
 
    
    private int cantidadContactos;
    private List<ContactoDto> contactos = new ArrayList();

    public AgendaDto(Agenda agenda) {
        
        this(agenda.getContactos(),agenda.cantidadContactos());
        
    }
    public AgendaDto(List<Contacto> lista, int cantidad){
        cantidadContactos = cantidad;
        for(Contacto contacto:lista){
            contactos.add(new ContactoDto(contacto));
        }
    }

    public int getCantidadContactos() {
        return cantidadContactos;
    }

    public List<ContactoDto> getContactos() {
        return contactos;
    }
    
    
    
    
}
