package obligatorio.obli.dtos;

import obligatorio.obli.models.Transito;
import obligatorio.obli.models.Usuarios.Propietario;

public class DetalleTransitoDTO {
    private String puesto;
    private String matricula;
    private String categoria;
    private String montoTarifa;
    private String bonificacion;
    private String montoBonificacion;
    private String montoPagado;
    private String fecha;

    public DetalleTransitoDTO(Transito transito, Propietario propietario) {
        this.puesto = transito.getPuesto().getNombre();
        this.matricula = transito.getVehiculo().getMatricula();
        this.categoria = transito.getVehiculo().getNombreCategoria();
        this.montoTarifa = transito.obtenerMontoTarifaFormateado();
        this.bonificacion = propietario.obtenerNombreBonificacionDelTransito(transito.getPuesto());
        this.montoBonificacion = transito.obtenerMontoBonificacionFormateado(propietario);
        this.montoPagado = transito.obtenerMontoPagadoFormateado(propietario);
        this.fecha = transito.obtenerFechaFormateada();
    }

    public String getPuesto() {
        return puesto;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getMontoTarifa() {
        return montoTarifa;
    }

    public String getBonificacion() {
        return bonificacion;
    }

    public String getMontoBonificacion() {
        return montoBonificacion;
    }

    public String getMontoPagado() {
        return montoPagado;
    }

    public String getFecha() {
        return fecha;
    }
}
