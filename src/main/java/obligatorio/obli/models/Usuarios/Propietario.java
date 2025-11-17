package obligatorio.obli.models.Usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import obligatorio.obli.exceptions.propietario.PropietarioErrorActualizacionEstadoException;
import obligatorio.obli.exceptions.propietario.estados.EstadoProhibidoRecibirBonificacionException;
import obligatorio.obli.models.Asignacion;
import obligatorio.obli.models.Estados.Estado;
import obligatorio.obli.models.Sistemas.Fachada.Eventos;
import obligatorio.obli.models.Notificacion;
import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Transito;
import obligatorio.obli.models.Vehiculo;
import obligatorio.obli.models.Bonificaciones.Bonificacion;

public class Propietario extends User {
    private List<Vehiculo> vehiculo;
    private double saldo;
    private double saldoMinAlerta;
    private Estado estado;
    private List<Asignacion> asignaciones;
    private List<Transito> transitos;
    private List<Notificacion> notificaciones;

    public Propietario(int id, String ci, String nombre, String password, List<Vehiculo> vehiculo, double saldo,
            double saldoMinAlerta, Estado estado) {
        super(id, ci, nombre, password);
        this.vehiculo = vehiculo;
        this.saldo = saldo;
        this.saldoMinAlerta = saldoMinAlerta;
        this.estado = estado;
        this.asignaciones = new ArrayList<>();
        this.transitos = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public List<Vehiculo> getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(List<Vehiculo> vehiculo) {
        this.vehiculo = vehiculo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getSaldoMinAlerta() {
        return saldoMinAlerta;
    }

    public void setSaldoMinAlerta(double saldoMinAlerta) {
        this.saldoMinAlerta = saldoMinAlerta;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Asignacion> getAsignaciones() {
        return new ArrayList<>(asignaciones);
    }

    public void agregarAsignacion(Asignacion asignacion) {
        if (asignacion != null && !asignaciones.contains(asignacion)) {
            asignaciones.add(asignacion);
        }
    }

    public void asignarBonificacion(Bonificacion bonificacion, Puesto puesto)
            throws EstadoProhibidoRecibirBonificacionException {
        if (!this.puedeRecibirBonificacion()) {
            throw new EstadoProhibidoRecibirBonificacionException(this.estado);
        }

        Asignacion asignacion = new Asignacion(bonificacion, puesto);
        this.agregarAsignacion(asignacion);
        avisar(Eventos.nuevaAsignacion);

        agregarNotificacion(String.format("Se asignó una nueva bonificación: %s para el puesto %s",
                bonificacion.getNombre(), puesto.getNombre()));
    }

    public void cambiarEstado(Estado nuevoEstado) throws PropietarioErrorActualizacionEstadoException {
        if (this.estado.equals(nuevoEstado)) {
            throw new PropietarioErrorActualizacionEstadoException(
                    String.format("El propietario ya esta en el estado '%s'", nuevoEstado.getNombre()));
        }
        this.estado = nuevoEstado;
        avisar(Eventos.cambioEstado);

        agregarNotificacion(String.format("Se ha cambiado tu estado en el sistema. Tu estado actual es %s",
                nuevoEstado.getNombre()));
    }

    public boolean puedeIniciarSesion() {
        return this.estado.puedeIniciarSesion();
    }

    public boolean puedeRealizarTransito() {
        return this.estado.puedeRealizarTransito();
    }

    public boolean puedeRecibirBonificacion() {
        return this.estado.puedeRecibirBonificacion();
    }

    public boolean aplicanBonificacionesEnTransito() {
        return this.estado.aplicanBonificacionesEnTransito();
    }

    public boolean recibeNotificaciones() {
        return this.estado.recibeNotificaciones();
    }

    public Bonificacion obtenerBonificacionParaPuesto(Puesto puesto) {
        if (puesto == null) {
            return null;
        }
        for (Asignacion asig : asignaciones) {
            if (asig.getPuesto().equals(puesto)) {
                return asig.getBonificacion();
            }
        }
        return null;
    }

    public boolean tieneVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return false;
        }
        return this.vehiculo.contains(vehiculo);
    }

    public boolean tieneSaldoSuficiente(double monto) {
        return this.saldo >= monto;
    }

    public void descontarSaldo(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto a descontar no puede ser negativo");
        }
        if (!tieneSaldoSuficiente(monto)) {
            throw new IllegalArgumentException(
                    String.format("Saldo insuficiente. Saldo actual: $%.2f, Monto requerido: $%.2f",
                            this.saldo, monto));
        }
        this.saldo -= monto;
    }

    public boolean saldoBajoMinimo() {
        return this.saldo < this.saldoMinAlerta;
    }

    public double registrarTransito(Transito transito) {
        if (!this.puedeRealizarTransito()) {
            throw new IllegalStateException(
                    String.format("El propietario está %s, no puede realizar tránsitos",
                            this.estado.getNombre().toLowerCase()));
        }

        double montoFinal = transito.calcularMontoFinal(this.transitos);

        if (!tieneSaldoSuficiente(montoFinal)) {
            throw new IllegalArgumentException(
                    String.format("Saldo insuficiente. Saldo actual: $%.2f, Monto requerido: $%.2f",
                            this.saldo, montoFinal));
        }

        this.saldo -= montoFinal;
        this.transitos.add(transito);

        return montoFinal;
    }

    public List<Transito> getTransitos() {
        return new ArrayList<>(transitos);
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones.stream()
                .sorted((n1, n2) -> Integer.compare(n2.getId(), n1.getId()))
                .collect(Collectors.toList());
    }

    public void agregarNotificacion(String mensaje) {
        if (mensaje == null || mensaje.isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede ser null o vacío");
        }

        if (!this.recibeNotificaciones()) {
            return;
        }

        Notificacion notif = new Notificacion(mensaje);
        this.notificaciones.add(notif);
        avisar(Eventos.nuevaNotificacion);
    }

    public void borrarNotificaciones() {
        if (this.notificaciones.isEmpty()) {
            throw new IllegalStateException("No hay notificaciones para borrar");
        }
        this.notificaciones.clear();
        avisar(Eventos.borradoNotificaciones);
    }

    public Bonificacion obtenerBonificacionDelTransito(Puesto puesto) {
        if (!this.aplicanBonificacionesEnTransito()) {
            return null;
        }
        return this.obtenerBonificacionParaPuesto(puesto);
    }

    public String obtenerNombreBonificacionDelTransito(Puesto puesto) {
        Bonificacion bonificacion = obtenerBonificacionDelTransito(puesto);
        return bonificacion != null ? bonificacion.getNombre() : "Ninguna";
    }

    public void enviarNotificacionesTransito(Transito transito) {
        if (!this.recibeNotificaciones()) {
            return;
        }

        String vehiculoMatricula = transito.getVehiculo().getMatricula();
        String puestoNombre = transito.getPuesto().getNombre();
        String mensaje1 = String.format("Pasaste por el puesto %s con el vehiculo %s",
                puestoNombre, vehiculoMatricula);

        agregarNotificacion(mensaje1);

        if (this.saldoBajoMinimo()) {
            String mensaje2 = String.format("Tu saldo actual es de $%.2f. Te recomendamos hacer una recarga",
                    this.getSaldo());
            agregarNotificacion(mensaje2);
        }
    }
}
