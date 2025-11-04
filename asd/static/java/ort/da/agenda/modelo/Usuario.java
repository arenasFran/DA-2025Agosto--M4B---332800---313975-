/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

/**
 *
 * @author PC
 */
public abstract class Usuario {
    
    private String nombre;
    private String password;
    private String nombreCompleto;
    

    public Usuario(String nombre, String password, String nombreCompleto) {
        this.nombre = nombre;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

  
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }
    
    
    
}
