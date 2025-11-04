/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class SistemaAgenda {

    
    private ArrayList<TipoContacto> tiposContacto = new ArrayList<TipoContacto> ();
    private ArrayList<TipoTelefono> tiposTelefono = new ArrayList<TipoTelefono> ();
    private ArrayList<CriterioBusqueda> criteriosBusqueda = new ArrayList<CriterioBusqueda> ();

    public void agregarCriterioBusqueda(CriterioBusqueda c){
        criteriosBusqueda.add(c);
    }

    
    public void agregarTipoTelefono(String nombre){
        tiposTelefono.add(new TipoTelefono(nombre));
    }

    public ArrayList<TipoTelefono> getTiposTelefono() {
        return tiposTelefono;
    }
    
    public void agregarTipoContacto(String nombre){
        tiposContacto.add(new TipoContacto(nombre));
    }

    public ArrayList<TipoContacto> getTiposContacto() {
        return tiposContacto;
    }


    public ArrayList<CriterioBusqueda> getCriteriosBusqueda() {
        return criteriosBusqueda;
    }

    public CriterioBusqueda getCriterioDefault(){
        return criteriosBusqueda.get(0);
    }
    
}
