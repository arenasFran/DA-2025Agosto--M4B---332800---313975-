package ort.da.agenda.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import observador.Observable;
import observador.Observador;
import ort.da.agenda.ConexionNavegador;
import ort.da.agenda.dto.SesionDto;
import ort.da.agenda.modelo.Administrador;
import ort.da.agenda.modelo.Fachada;

@RestController
@RequestMapping("/administrador")
@Scope("session")

public class ControladorAdministrador implements Observador{

     private final ConexionNavegador conexionNavegador; 
    
    public ControladorAdministrador(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }
    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE(); 
       
    }

    @PostMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(@SessionAttribute(name = "usuarioAdmin") Administrador admin){
        Fachada.getInstancia().agregarObservador(this);
         return Respuesta.lista(
           sesiones(),
            new Respuesta("nombreAdministrador",admin.getNombreCompleto())
            );
    }
    public Respuesta sesiones(){

         return new Respuesta("sesiones", SesionDto.listaSesionesDto(Fachada.getInstancia().getSesiones()));
    }
    @PostMapping("/vistaCerrada")
    public void vistaCerrada(){
        Fachada.getInstancia().quitarObservador(this);
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
       if(evento.equals(Fachada.Eventos.cambioListaSesiones)){
            conexionNavegador.enviarJSON(Respuesta.lista(sesiones()));
            System.out.println("LISTA SESIONES!!!");
       }
    }
}
