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
import ort.da.agenda.modelo.Fachada;
import ort.da.agenda.modelo.Sesion;
import ort.da.agenda.modelo.TipoContacto;
import ort.da.agenda.modelo.TipoTelefono;

@RestController
@RequestMapping("/agenda")
@Scope("session")

public class ControladorContactos implements Observador{
    
    private Agenda agenda;
    private List<TipoContacto> tiposContacto;
    private List<TipoTelefono> tiposTelefono;
  
   private final ConexionNavegador conexionNavegador; 
    
    public ControladorContactos(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @PostMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(@SessionAttribute(name = "usuarioAgenda") Sesion sesion){

        agenda = sesion.getUsuario().getAgenda();    
        agenda.agregarObservador(this);
        return Respuesta.lista(tiposContacto(),agenda(),tiposTelefono());
    }
    @PostMapping("/vistaCerrada")
    public void vistaCerrada(){
        if(agenda!=null){
             agenda.quitarObservador(this);
        }
    }
    @PostMapping("/crearContacto")
    public List<Respuesta> crearContacto(@RequestParam String nombre, @RequestParam String telefono,@RequestParam int posTipoContacto,@RequestParam int posTipoTelefono) throws AgendaException {
        
      if(posTipoContacto<0) throw new AgendaException("Seleccione el tipo de contacto");
       TipoContacto tc = tiposContacto.get(posTipoContacto);
       TipoTelefono tt = tiposTelefono.get(posTipoTelefono);
       agenda.crearContacto(nombre, telefono, tc,tt);
       return Respuesta.lista(agenda());
    }

    private Respuesta agenda(){
        return new Respuesta("agenda",new AgendaDto(agenda));
    }
    private Respuesta tiposContacto(){

        tiposContacto = new ArrayList<TipoContacto>(Fachada.getInstancia().getTiposContacto());

        List<NombreDto> tiposDto = new ArrayList<NombreDto>();
        
        for(TipoContacto tc:tiposContacto){
            tiposDto.add(new NombreDto(tc.getNombre()));
        }
        return new Respuesta("tiposContacto", tiposDto);
    }
     private Respuesta tiposTelefono(){

        tiposTelefono = new ArrayList<TipoTelefono>(Fachada.getInstancia().getTiposTelefono());

        List<NombreDto> tiposDto = new ArrayList<NombreDto>();
        
        for(TipoTelefono tt:tiposTelefono){
            tiposDto.add(new NombreDto(tt.getNombre()));
        }
        return new Respuesta("tiposTelefono", tiposDto);
    }
     @Override
     public void actualizar(Object evento, Observable origen) {
        if(evento.equals(Agenda.Eventos.cambioListaContactos)){
            conexionNavegador.enviarJSON(Respuesta.lista(agenda()));
        }
     }


}
