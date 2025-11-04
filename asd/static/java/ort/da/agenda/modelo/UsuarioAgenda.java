/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

/**
 *
 * @author PC
 */
public class UsuarioAgenda extends Usuario{
    
    private Agenda agenda = new Agenda(Fachada.getInstancia().getCriterioDefault());

    public UsuarioAgenda(String nombre, String password, String nombreCompleto) {
        super(nombre, password, nombreCompleto);
    }
   public Agenda getAgenda() {
        return agenda;
    }

 
    
    
}

