package obligatorio.obli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import obligatorio.obli.models.*;
import obligatorio.obli.models.Categorias.*;
import obligatorio.obli.models.Estados.*;
import obligatorio.obli.models.Usuarios.*;

public class Precarga {
    
    public static List<Categoria> cargarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new CategoriaAuto());
        categorias.add(new CategoriaBus());
        categorias.add(new CategoriaCamion());
        return categorias;
    }
    
    public static List<Tarifa> cargarTarifas() {
        List<Tarifa> tarifas = new ArrayList<>();
        tarifas.add(new Tarifa(45.0, "Auto"));
        tarifas.add(new Tarifa(80.0, "Bus"));
        tarifas.add(new Tarifa(120.0, "Camion"));
        return tarifas;
    }
    
    public static List<Puesto> cargarPuestos() {
        List<Tarifa> tarifas = cargarTarifas();
        List<Puesto> puestos = new ArrayList<>();
        
        puestos.add(new Puesto("Peaje Pando", "Ruta 8 Km 23, Canelones", new ArrayList<>(tarifas)));
        puestos.add(new Puesto("Peaje Atlántida", "Ruta Interbalnearia Km 45, Canelones", new ArrayList<>(tarifas)));
        puestos.add(new Puesto("Peaje Santa Lucía", "Ruta 1 Km 50, San José", new ArrayList<>(tarifas)));
        puestos.add(new Puesto("Peaje Young", "Ruta 3 Km 187, Río Negro", new ArrayList<>(tarifas)));
        puestos.add(new Puesto("Peaje Fray Bentos", "Ruta 2 Km 306, Río Negro", new ArrayList<>(tarifas)));
        
        return puestos;
    }
    
    public static List<Estado> cargarEstados() {
        List<Estado> estados = new ArrayList<>();
        estados.add(new EstadoHabilitado("Habilitado"));
        estados.add(new EstadoDeshabilitado("Deshabilitado"));
        estados.add(new EstadoPenalizado("Penalizado"));
        estados.add(new EstadoSupendido("Suspendido"));
        return estados;
    }
    
    public static List<Vehiculo> cargarVehiculos() {
        List<Categoria> categorias = cargarCategorias();
        List<Vehiculo> vehiculos = new ArrayList<>();
        
        // Autos
        vehiculos.add(new Vehiculo("SAA1234", "Chevrolet Onix", "Blanco", categorias.get(0)));
        vehiculos.add(new Vehiculo("SBB5678", "Volkswagen Gol", "Azul", categorias.get(0)));
        vehiculos.add(new Vehiculo("SCC9012", "Peugeot 208", "Rojo", categorias.get(0)));
        vehiculos.add(new Vehiculo("SDD3456", "Ford Ka", "Negro", categorias.get(0)));
        vehiculos.add(new Vehiculo("SEE7890", "Renault Sandero", "Gris", categorias.get(0)));
        
        // Buses
        vehiculos.add(new Vehiculo("SBU1111", "Mercedes Benz OF-1721", "Amarillo", categorias.get(1)));
        vehiculos.add(new Vehiculo("SBU2222", "Volvo B270F", "Azul", categorias.get(1)));
        vehiculos.add(new Vehiculo("SBU3333", "Scania K320", "Verde", categorias.get(1)));
        
        // Camiones
        vehiculos.add(new Vehiculo("SCA1111", "Scania R450", "Blanco", categorias.get(2)));
        vehiculos.add(new Vehiculo("SCA2222", "Mercedes Benz Actros", "Rojo", categorias.get(2)));
        vehiculos.add(new Vehiculo("SCA3333", "Volvo FH16", "Azul", categorias.get(2)));
        
        return vehiculos;
    }
    
    public static List<Bonificacion> cargarBonificaciones() {
        List<Bonificacion> bonificaciones = new ArrayList<>();
        bonificaciones.add(new Bonificacion("Descuento Tercera Edad"));
        bonificaciones.add(new Bonificacion("Descuento Estudiante"));
        bonificaciones.add(new Bonificacion("Descuento Discapacitado"));
        bonificaciones.add(new Bonificacion("Descuento Residente Local"));
        bonificaciones.add(new Bonificacion("Sin Bonificación"));
        return bonificaciones;
    }
    
    public static List<Propietario> cargarPropietarios() {
        List<Vehiculo> vehiculos = cargarVehiculos();
        List<Propietario> propietarios = new ArrayList<>();
        
        // Propietario específico requerido
        List<Vehiculo> vehiculosUsuario = Arrays.asList(vehiculos.get(1));
        propietarios.add(new Propietario(1, "23456789", "Usuario Propietario", "prop.123", vehiculosUsuario, 2000.0, 500.0));
        
        // Propietario 2 - Juan Pérez (múltiples vehículos)
        List<Vehiculo> vehiculosJuan = Arrays.asList(vehiculos.get(0), vehiculos.get(5));
        propietarios.add(new Propietario(2, "12345678", "Juan Carlos Pérez", "pass123", vehiculosJuan, 1500.0, 200.0));
        
        // Propietario 3 - Carlos Rodríguez
        List<Vehiculo> vehiculosCarlos = Arrays.asList(vehiculos.get(2), vehiculos.get(8));
        propietarios.add(new Propietario(3, "34567890", "Carlos Alberto Rodríguez", "pass789", vehiculosCarlos, 2200.0, 300.0));
        
        // Propietario 4 - Ana Fernández
        List<Vehiculo> vehiculosAna = Arrays.asList(vehiculos.get(3));
        propietarios.add(new Propietario(4, "45678901", "Ana Sofía Fernández", "pass101", vehiculosAna, 650.0, 100.0));
        
        // Propietario 5 - Luis Martínez (transportista)
        List<Vehiculo> vehiculosLuis = Arrays.asList(vehiculos.get(6), vehiculos.get(9), vehiculos.get(10));
        propietarios.add(new Propietario(5, "56789012", "Luis Fernando Martínez", "pass202", vehiculosLuis, 5000.0, 500.0));
        
        return propietarios;
    }
    
    public static List<Transito> cargarTransitos() {
        List<Puesto> puestos = cargarPuestos();
        List<Vehiculo> vehiculos = cargarVehiculos();
        List<Tarifa> tarifas = cargarTarifas();
        List<Bonificacion> bonificaciones = cargarBonificaciones();
        List<Transito> transitos = new ArrayList<>();
        
        // Tránsitos de ejemplo
        transitos.add(new Transito(puestos.get(0), vehiculos.get(0), tarifas.get(0), new Date(), bonificaciones.get(4)));
        transitos.add(new Transito(puestos.get(1), vehiculos.get(1), tarifas.get(0), new Date(), bonificaciones.get(1)));
        transitos.add(new Transito(puestos.get(2), vehiculos.get(5), tarifas.get(1), new Date(), bonificaciones.get(4)));
        transitos.add(new Transito(puestos.get(3), vehiculos.get(8), tarifas.get(2), new Date(), bonificaciones.get(2)));
        transitos.add(new Transito(puestos.get(4), vehiculos.get(2), tarifas.get(0), new Date(), bonificaciones.get(0)));
        
        return transitos;
    }
    
    public static List<Administrador> cargarAdministradores() {
        List<Administrador> administradores = new ArrayList<>();
        
        // Administrador específico requerido
        administradores.add(new Administrador(1001, "12345678", "Usuario Administrador", "admin.123"));
        
        // Administradores adicionales del sistema
        administradores.add(new Administrador(1002, "22222222", "Supervisor Operaciones", "super456"));
        administradores.add(new Administrador(1003, "33333333", "Gerente Sistema", "gerente789"));
        administradores.add(new Administrador(1004, "1", "Gerente Sistema", "1"));
        
        return administradores;
    }
}
