/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ort.da.agenda.modelo;

import java.util.Date;

/**
 *
 * @author PC
 */
public class Sesion {
    
    private Date fechaIngreso = new Date();
    private UsuarioAgenda usuario;

    public Sesion(UsuarioAgenda usuario) {
        this.usuario = usuario;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public UsuarioAgenda getUsuario() {
        return usuario;
    }
    
    
    
}
