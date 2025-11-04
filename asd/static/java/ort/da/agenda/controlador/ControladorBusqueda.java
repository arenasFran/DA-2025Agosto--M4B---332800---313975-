package ort.da.agenda.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import observador.Observable;
import observador.Observador;
import ort.da.agenda.ConexionNavegador;
import ort.da.agenda.dto.AgendaDto;
import ort.da.agenda.dto.NombreDto;
import ort.da.agenda.modelo.Agenda;
import ort.da.agenda.modelo.AgendaException;
import ort.da.agenda.modelo.Contacto;
import ort.da.agenda.modelo.CriterioBusqueda;
import ort.da.agenda.modelo.Fachada;
import ort.da.agenda.modelo.Sesion;

@RestController
@RequestMapping("/busqueda")
@Scope("session")

public class ControladorBusqueda implements Observador{
    
    private Agenda agenda;
    private ArrayList<CriterioBusqueda> criterios;
    private String filtro = "";

     private final ConexionNavegador conexionNavegador; 
    
    public ControladorBusqueda(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @PostMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(@SessionAttribute(name = "usuarioAgenda") Sesion sesion){

        agenda = sesion.getUsuario().getAgenda();    
        agenda.agregarObservador(this);
        return Respuesta.lista(titulo(),criterios(),criterioActual());
        
    }
    @PostMapping("/vistaCerrada")
    public void vistaCerrada(){
        if(agenda!=null){
             agenda.quitarObservador(this);
        }
    }
    
    @PostMapping("/buscar")
    public List<Respuesta> buscarContactos(@RequestParam String filtro) throws AgendaException {

      if(filtro.isBlank()) throw new AgendaException("Ingrese un texto de busqueda"); 
      this.filtro = filtro;
      List<Contacto> lista = agenda.buscar(filtro);
      List<Respuesta> respuestas = Respuesta.lista(resultado(lista));
     
      return respuestas;
    }
    @PostMapping("/cambiarCriterio")
    public List<Respuesta> setCriterio(@RequestParam int posCriterio) throws AgendaException {
        agenda.setCriterio(criterios.get(posCriterio));
        
        return Respuesta.lista(titulo(),resultado(agenda.buscar(filtro)));
    }
   
    private Respuesta resultado(List<Contacto> contactos){

            return new Respuesta("resultado", new AgendaDto(contactos,contactos.size()));
    }
  
     private Respuesta titulo(){
        return new Respuesta("titulo","Buscando en " + agenda.cantidadContactos() + " contactos-->" + agenda.getCriterio().getNombre());
    }
    private Respuesta criterios(){

        criterios = new ArrayList<CriterioBusqueda>(Fachada.getInstancia().getCriteriosBusqueda());

        List<NombreDto> dtos = new ArrayList<NombreDto>();
        
        for(CriterioBusqueda cb:criterios){
            dtos.add(new NombreDto(cb.getNombre()));
        }
        return new Respuesta("criterios", dtos);
    }
    private Respuesta criterioActual() {
      return new Respuesta("criterioActual", agenda.getCriterio().getNombre());
    }
    @Override
    public void actualizar(Object evento, Observable origen) {
       if(evento.equals(Agenda.Eventos.cambioListaContactos)){
            conexionNavegador.enviarJSON( Respuesta.lista(titulo(),resultado(agenda.buscar(filtro))));
       }else if(evento.equals(Agenda.Eventos.cambioCriterio)){
            conexionNavegador.enviarJSON( Respuesta.lista(titulo(),criterioActual(),resultado(agenda.buscar(filtro))));
       }
    }


}
