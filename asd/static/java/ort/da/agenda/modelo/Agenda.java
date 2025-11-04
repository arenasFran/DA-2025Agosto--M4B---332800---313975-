/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

import java.util.ArrayList;
import java.util.List;

import observador.Observable;

/**
 *
 * @author PC
 */
public class Agenda extends Observable{
    
    public enum Eventos{cambioListaContactos,cambioCriterio};

    private ArrayList<Contacto> contactos = new ArrayList<Contacto>();
    private CriterioBusqueda criterio;
    
    

    public Agenda(CriterioBusqueda criterio) {
        this.criterio = criterio;
    }
    

    public ArrayList<Contacto> getContactos() {
        return contactos;
    }
    
    public void crearContacto(String nombre,String numero,TipoContacto tipoContacto,TipoTelefono tipoTelefono) throws AgendaException{
        Contacto c = new Contacto(nombre, numero, tipoContacto,tipoTelefono);
        c.validar();
        if(contactos.contains(c)) throw new AgendaException("Ya existe el contacto");
        contactos.add(c);
        avisar(Eventos.cambioListaContactos);
        Fachada.getInstancia().avisar(Fachada.Eventos.cambioListaSesiones);
     
    }
    public int cantidadContactos(){
        return contactos.size();
    }
      public List<Contacto> buscar(String filtro){
        
        ArrayList<Contacto> resultado = new ArrayList<Contacto>();
        if(criterio==null) return resultado;
        if(filtro.isBlank()) return resultado;
        for(Contacto c:contactos){
            if(criterio.filtrar(c, filtro)){
                    resultado.add(c);
            }
        }
        return resultado;

    }


      public CriterioBusqueda getCriterio() {
          return criterio;
      }


      public void setCriterio(CriterioBusqueda criterio) {
          this.criterio = criterio;
          avisar(Agenda.Eventos.cambioCriterio);
      }
      
    
}
