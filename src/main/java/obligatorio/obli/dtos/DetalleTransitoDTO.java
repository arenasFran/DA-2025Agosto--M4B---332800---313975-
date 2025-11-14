package obligatorio.obli.dtos;

import obligatorio.obli.models.Transito;
import obligatorio.obli.models.Usuarios.Propietario;
import obligatorio.obli.models.Vehiculo;

/**
 * DTO siguiendo patrón agenda:
 * - Recibe objetos del modelo
 * - Extrae información usando los métodos Expert de cada objeto
 * - No tiene lógica de negocio
 */
public class DetalleTransitoDTO {
    private String propietarioNombre;
    private String propietarioCi;
    private String propietarioEstado;
    private String vehiculoMatricula;
    private String vehiculoModelo;
    private String vehiculoColor;
    private String categoriaVehiculo;
    private String puestoNombre;
    private String bonificacion;
    private double montoDescontado;
    private double saldoAntes;
    private double saldoDespues;
    private String fechaHora;

    public DetalleTransitoDTO(Transito transito, Propietario propietario, double saldoAntes, double saldoDespues) {
        // Extraer info usando métodos Expert de cada objeto
        Vehiculo vehiculo = transito.getVehiculo();

        // Propietario es Expert de sus datos
        this.propietarioNombre = propietario.getNombre();
        this.propietarioCi = propietario.getCi();
        this.propietarioEstado = propietario.getEstado().getNombre();

        // Vehiculo es Expert de sus datos
        this.vehiculoMatricula = vehiculo.getMatricula();
        this.vehiculoModelo = vehiculo.getModelo();
        this.vehiculoColor = vehiculo.getColor();
        this.categoriaVehiculo = vehiculo.getNombreCategoria();

        // Transito es Expert de sus datos y cálculos
        this.puestoNombre = transito.getPuesto().getNombre();
        this.montoDescontado = transito.calcularMontoFinal();
        this.fechaHora = transito.obtenerFechaFormateada();

        // Propietario es Expert de sus bonificaciones
        this.bonificacion = propietario.obtenerNombreBonificacionDelTransito(transito.getPuesto());

        // Saldos calculados en controller (info temporal)
        this.saldoAntes = saldoAntes;
        this.saldoDespues = saldoDespues;
    }

    // Getters
    public String getPropietarioNombre() {
        return propietarioNombre;
    }

    public String getPropietarioCi() {
        return propietarioCi;
    }

    public String getPropietarioEstado() {
        return propietarioEstado;
    }

    public String getVehiculoMatricula() {
        return vehiculoMatricula;
    }

    public String getVehiculoModelo() {
        return vehiculoModelo;
    }

    public String getVehiculoColor() {
        return vehiculoColor;
    }

    public String getCategoriaVehiculo() {
        return categoriaVehiculo;
    }

    public String getPuestoNombre() {
        return puestoNombre;
    }

    public String getBonificacion() {
        return bonificacion;
    }

    public double getMontoDescontado() {
        return montoDescontado;
    }

    public double getSaldoAntes() {
        return saldoAntes;
    }

    public double getSaldoDespues() {
        return saldoDespues;
    }

    public String getFechaHora() {
        return fechaHora;
    }
}
