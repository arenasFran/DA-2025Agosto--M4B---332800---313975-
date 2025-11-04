/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

import java.util.ArrayList;

import observador.Observable;

/**
 *
 * @author PC
 */
public class Fachada extends Observable{
    
    public enum Eventos{cambioListaSesiones};
    
    private SistemaAcceso sAcceso = new SistemaAcceso();
    private SistemaAgenda sAgenda = new SistemaAgenda();
    
    //SINGLETON

    private static Fachada instancia = new Fachada();

    public static Fachada getInstancia() {
        return instancia;
    }
    
    private Fachada() {
    }
    
    //DELEGACIONES

    public void agregarUsuarioAgenda(String nom, String pwd, String nombreCompleto) {
        sAcceso.agregarUsuarioAgenda(nom, pwd, nombreCompleto);
    }

    public Sesion loginAgenda(String nom, String pwd) throws AgendaException {
        return sAcceso.loginAgenda(nom, pwd);
    }

    public void agregarTipoContacto(String nombre) {
        sAgenda.agregarTipoContacto(nombre);
    }

    public ArrayList<TipoContacto> getTiposContacto() {
        return sAgenda.getTiposContacto();
    }

    public void agregarAdministrador(String nom, String pwd, String nombreCompleto) {
        sAcceso.agregarAdministrador(nom, pwd, nombreCompleto);
    }

    public Administrador loginAdministrador(String nom, String pwd) throws AgendaException {
        return sAcceso.loginAdministrador(nom, pwd);
    }

    public ArrayList<Sesion> getSesiones() {
        return sAcceso.getSesiones();
    }

    public void logout(Sesion s) {
        sAcceso.logout(s);
    }

    public void agregarTipoTelefono(String nombre) {
        sAgenda.agregarTipoTelefono(nombre);
    }

    public ArrayList<TipoTelefono> getTiposTelefono() {
        return sAgenda.getTiposTelefono();
    }

    public void agregarCriterioBusqueda(CriterioBusqueda c) {
        sAgenda.agregarCriterioBusqueda(c);
    }

    public ArrayList<CriterioBusqueda> getCriteriosBusqueda() {
        return sAgenda.getCriteriosBusqueda();
    }

    public CriterioBusqueda getCriterioDefault() {
        return sAgenda.getCriterioDefault();
    }
    

   
    
}
