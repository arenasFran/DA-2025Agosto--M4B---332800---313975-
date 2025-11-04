/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

/**
 *
 * @author PC
 */
public class DatosPrueba {
    
    public static void cargar(){
        
        Fachada fachada = Fachada.getInstancia();
        
        fachada.agregarCriterioBusqueda(new XCombinado());
        fachada.agregarCriterioBusqueda(new XTelefono());
        fachada.agregarCriterioBusqueda(new XNonbre());
       
        fachada.agregarTipoContacto("Laboral");
        fachada.agregarTipoContacto("Particular");
        fachada.agregarTipoContacto("Familiar");

         fachada.agregarTipoTelefono("Fijo");
         fachada.agregarTipoTelefono("Celular");
         fachada.agregarTipoTelefono("Internacional");
        
        fachada.agregarUsuarioAgenda("a", "a", "Ana");
        fachada.agregarUsuarioAgenda("b", "b", "Beatriz");
        fachada.agregarUsuarioAgenda("c", "c", "Carlos");

        fachada.agregarAdministrador("z", "z", "Zeta");
        fachada.agregarAdministrador("x", "x", "Equis");

       
        
        Sesion s = null;
        try {
            s = fachada.loginAgenda("a", "a");
        } catch (AgendaException e) {
           System.out.println("Error:" + e.getMessage());
           return;
        }
       
        System.out.println("Usuario:" + s.getUsuario().getNombreCompleto());
        Agenda agenda = s.getUsuario().getAgenda();
        try {
            
            agenda.crearContacto("Contacto 1",
                    "40123456",
                    fachada.getTiposContacto().get(0),fachada.getTiposTelefono().get(0));
            agenda.crearContacto("Contacto 2",
                    "40987055",
                    fachada.getTiposContacto().get(1),fachada.getTiposTelefono().get(0) );
            
            System.out.println("Lista:");
            
            System.out.println(agenda.getContactos());

           //    Fachada.getInstancia().logout(s);
            
        } catch (AgendaException ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        
       
    }
}
