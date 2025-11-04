package ort.da.agenda.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ort.da.agenda.modelo.Sesion;

public class SesionDto {

    private String fechaIngreso;
    private String usuario;
    private String nombreCompleto;
    private int cantidadContactos;
  
    
    public SesionDto(Sesion sesion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        fechaIngreso = sdf.format(sesion.getFechaIngreso());
        usuario = sesion.getUsuario().getNombre();
        nombreCompleto = sesion.getUsuario().getNombreCompleto();
        cantidadContactos = sesion.getUsuario().getAgenda().cantidadContactos();
        
    }
    
    

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public static List<SesionDto> listaSesionesDto(List<Sesion> sesiones) {
                
        List<SesionDto> sesionDtos = new ArrayList<>();
        for (Sesion sesion : sesiones) {
            sesionDtos.add(new SesionDto(sesion));
        }
        return sesionDtos;
    }



    public int getCantidadContactos() {
        return cantidadContactos;
    }


    

}
