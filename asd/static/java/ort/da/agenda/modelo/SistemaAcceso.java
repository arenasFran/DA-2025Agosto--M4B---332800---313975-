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
public class SistemaAcceso {
    
    private ArrayList<UsuarioAgenda> usuariosAgenda = new ArrayList();
    private ArrayList<Administrador> administradores = new ArrayList();
    private ArrayList<Sesion> sesiones = new ArrayList();
    
    public void agregarUsuarioAgenda(String nom,String pwd,String nombreCompleto){
        usuariosAgenda.add(new UsuarioAgenda(nom, pwd, nombreCompleto));
    }
     public void agregarAdministrador(String nom,String pwd,String nombreCompleto){
        administradores.add(new Administrador(nom, pwd, nombreCompleto));
    }


   public Sesion loginAgenda(String nom,String pwd) throws AgendaException{
        
       Sesion sesion = null;
       UsuarioAgenda usuario = (UsuarioAgenda) login(nom, pwd, usuariosAgenda);
       if(usuario!=null){
           sesion = new Sesion(usuario);
           sesiones.add(sesion);
           Fachada.getInstancia().avisar(Fachada.Eventos.cambioListaSesiones);
           return sesion;
       }
       throw new AgendaException("Login incorrecto");
       
    }
     public Administrador loginAdministrador(String nom,String pwd) throws AgendaException{
       Administrador admin =  (Administrador) login(nom, pwd, administradores);
       if(admin==null) throw new AgendaException("Login incorrecto");
       return admin;
    }
    
     private Usuario login(String nom, String pwd, ArrayList lista){
        Usuario usuario;
        
        for(Object o:lista){
            usuario = (Usuario)o;
            if(usuario.getNombre().equals(nom) && usuario.getPassword().equals(pwd)){
                return usuario;
            }
        }
        return null;
    }

    public ArrayList<Sesion> getSesiones() {
        return sesiones;
    }
    
    public void logout(Sesion s){
        sesiones.remove(s);
        Fachada.getInstancia().avisar(Fachada.Eventos.cambioListaSesiones);
       
    }
}
