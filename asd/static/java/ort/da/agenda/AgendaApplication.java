package ort.da.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ort.da.agenda.modelo.DatosPrueba;

@SpringBootApplication
public class AgendaApplication {

	public static void main(String[] args) {
	
		SpringApplication.run(AgendaApplication.class, args);
		DatosPrueba.cargar();
	}

}
