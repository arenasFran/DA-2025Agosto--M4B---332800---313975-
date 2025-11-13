package obligatorio.obli.examples;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import obligatorio.obli.models.Puesto;
import obligatorio.obli.models.Tarifa;
import obligatorio.obli.models.Transito;
import obligatorio.obli.models.Vehiculo;
import obligatorio.obli.models.Bonificaciones.*;
import obligatorio.obli.models.Categorias.CategoriaAuto;

/**
 * Ejemplos de uso del sistema de bonificaciones
 * 
 * Este archivo demuestra cómo usar las diferentes bonificaciones
 * y calcular montos finales de tránsitos
 */
public class EjemplosBonificaciones {

    public static void main(String[] args) {
        ejemploExonerados();
        ejemploFrecuentes();
        ejemploTrabajadores();
        ejemploSinBonificacion();
    }

    /**
     * Ejemplo 1: Bonificación Exonerados (100% descuento)
     */
    private static void ejemploExonerados() {
        System.out.println("\n=== EJEMPLO: EXONERADOS ===");

        // Setup
        Tarifa tarifa = new Tarifa(45.0, "Auto");
        Puesto puesto = new Puesto("Peaje Pando", "Ruta 8", new ArrayList<>());
        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Rojo", new CategoriaAuto());
        Bonificacion bonificacion = new BonificacionExonerados();

        // Crear tránsito con bonificación
        Transito transito = new Transito(puesto, vehiculo, tarifa, new Date(), bonificacion);

        // Calcular monto final
        double montoFinal = transito.calcularMontoFinal();
        double descuento = transito.obtenerPorcentajeDescuento(null);

        System.out.println("Tarifa base: $" + tarifa.getMonto());
        System.out.println("Descuento: " + (descuento * 100) + "%");
        System.out.println("Monto final: $" + montoFinal);
        System.out.println("✅ Usuario exonerado NO paga");
    }

    /**
     * Ejemplo 2: Bonificación Frecuentes (50% desde 2do tránsito)
     */
    private static void ejemploFrecuentes() {
        System.out.println("\n=== EJEMPLO: FRECUENTES ===");

        // Setup
        Tarifa tarifa = new Tarifa(45.0, "Auto");
        Puesto puesto = new Puesto("Peaje Atlántida", "Ruta Interbalnearia", new ArrayList<>());
        Vehiculo vehiculo = new Vehiculo("XYZ789", "Honda", "Azul", new CategoriaAuto());
        Bonificacion bonificacion = new BonificacionFrecuentes();

        List<Transito> historialDelDia = new ArrayList<>();

        // PRIMER TRÁNSITO DEL DÍA
        Date fecha1 = new Date();
        Transito transito1 = new Transito(puesto, vehiculo, tarifa, fecha1, bonificacion);
        double monto1 = transito1.calcularMontoFinal(historialDelDia);

        System.out.println("\n--- Primer tránsito del día ---");
        System.out.println("Tarifa base: $" + tarifa.getMonto());
        System.out.println("Descuento: " + (transito1.obtenerPorcentajeDescuento(historialDelDia) * 100) + "%");
        System.out.println("Monto final: $" + monto1);

        // Agregar primer tránsito al historial
        historialDelDia.add(transito1);

        // SEGUNDO TRÁNSITO DEL DÍA (1 hora después)
        Date fecha2 = new Date(fecha1.getTime() + 3600000); // +1 hora
        Transito transito2 = new Transito(puesto, vehiculo, tarifa, fecha2, bonificacion);
        double monto2 = transito2.calcularMontoFinal(historialDelDia);

        System.out.println("\n--- Segundo tránsito del día (mismo puesto, mismo vehículo) ---");
        System.out.println("Tarifa base: $" + tarifa.getMonto());
        System.out.println("Descuento: " + (transito2.obtenerPorcentajeDescuento(historialDelDia) * 100) + "%");
        System.out.println("Monto final: $" + monto2);
        System.out.println("✅ Ahorro de $" + (monto1 - monto2) + " en el segundo tránsito");

        // Agregar segundo tránsito
        historialDelDia.add(transito2);

        // TERCER TRÁNSITO DEL DÍA (sigue aplicando 50%)
        Date fecha3 = new Date(fecha2.getTime() + 3600000); // +1 hora más
        Transito transito3 = new Transito(puesto, vehiculo, tarifa, fecha3, bonificacion);
        double monto3 = transito3.calcularMontoFinal(historialDelDia);

        System.out.println("\n--- Tercer tránsito del día ---");
        System.out.println("Monto final: $" + monto3);
        System.out.println("✅ El descuento se mantiene en tránsitos subsiguientes");
    }

    /**
     * Ejemplo 3: Bonificación Trabajadores (80% en días de semana)
     */
    private static void ejemploTrabajadores() {
        System.out.println("\n=== EJEMPLO: TRABAJADORES ===");

        // Setup
        Tarifa tarifa = new Tarifa(120.0, "Camion");
        Puesto puesto = new Puesto("Peaje Young", "Ruta 3", new ArrayList<>());
        Vehiculo vehiculo = new Vehiculo("CAM456", "Scania", "Blanco", new CategoriaAuto());
        Bonificacion bonificacion = new BonificacionTrabajadores();

        // CASO 1: Tránsito un LUNES
        Calendar calLunes = Calendar.getInstance();
        calLunes.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Transito transitoLunes = new Transito(puesto, vehiculo, tarifa, calLunes.getTime(), bonificacion);
        double montoLunes = transitoLunes.calcularMontoFinal();

        System.out.println("\n--- Tránsito un LUNES ---");
        System.out.println("Tarifa base: $" + tarifa.getMonto());
        System.out.println("Descuento: " + (transitoLunes.obtenerPorcentajeDescuento(null) * 100) + "%");
        System.out.println("Monto final: $" + montoLunes);
        System.out.println("✅ Descuento aplicado (día laboral)");

        // CASO 2: Tránsito un SÁBADO
        Calendar calSabado = Calendar.getInstance();
        calSabado.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Transito transitoSabado = new Transito(puesto, vehiculo, tarifa, calSabado.getTime(), bonificacion);
        double montoSabado = transitoSabado.calcularMontoFinal();

        System.out.println("\n--- Tránsito un SÁBADO ---");
        System.out.println("Tarifa base: $" + tarifa.getMonto());
        System.out.println("Descuento: " + (transitoSabado.obtenerPorcentajeDescuento(null) * 100) + "%");
        System.out.println("Monto final: $" + montoSabado);
        System.out.println("⚠️  Sin descuento (fin de semana)");

        // CASO 3: Tránsito un VIERNES (último día laboral)
        Calendar calViernes = Calendar.getInstance();
        calViernes.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Transito transitoViernes = new Transito(puesto, vehiculo, tarifa, calViernes.getTime(), bonificacion);
        double montoViernes = transitoViernes.calcularMontoFinal();

        System.out.println("\n--- Tránsito un VIERNES ---");
        System.out.println("Monto final: $" + montoViernes);
        System.out.println("✅ Descuento aplicado (último día laboral)");
    }

    /**
     * Ejemplo 4: Sin Bonificación (paga tarifa completa)
     */
    private static void ejemploSinBonificacion() {
        System.out.println("\n=== EJEMPLO: SIN BONIFICACIÓN ===");

        // Setup
        Tarifa tarifa = new Tarifa(80.0, "Bus");
        Puesto puesto = new Puesto("Peaje Fray Bentos", "Ruta 2", new ArrayList<>());
        Vehiculo vehiculo = new Vehiculo("BUS111", "Mercedes", "Amarillo", new CategoriaAuto());
        Bonificacion bonificacion = new SinBonificacion();

        Transito transito = new Transito(puesto, vehiculo, tarifa, new Date(), bonificacion);
        double montoFinal = transito.calcularMontoFinal();

        System.out.println("\nTarifa base: $" + tarifa.getMonto());
        System.out.println("Descuento: " + (transito.obtenerPorcentajeDescuento(null) * 100) + "%");
        System.out.println("Monto final: $" + montoFinal);
        System.out.println("⚠️  Usuario paga tarifa completa");
    }

    /**
     * BONUS: Comparación de todas las bonificaciones
     */
    public static void compararBonificaciones() {
        System.out.println("\n=== COMPARACIÓN: TODAS LAS BONIFICACIONES ===");

        Tarifa tarifa = new Tarifa(45.0, "Auto");
        Puesto puesto = new Puesto("Peaje Test", "Ruta Test", new ArrayList<>());
        Vehiculo vehiculo = new Vehiculo("TEST123", "Test", "Test", new CategoriaAuto());

        // Crear un lunes para trabajadores
        Calendar calLunes = Calendar.getInstance();
        calLunes.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Bonificacion[] bonificaciones = {
                new BonificacionExonerados(),
                new BonificacionFrecuentes(),
                new BonificacionTrabajadores(),
                new SinBonificacion()
        };

        System.out.println("\nTarifa base: $" + tarifa.getMonto());
        System.out.println("\nMontos finales por bonificación:");

        for (Bonificacion bono : bonificaciones) {
            Transito t = new Transito(puesto, vehiculo, tarifa, calLunes.getTime(), bono);
            double monto = t.calcularMontoFinal(new ArrayList<>());
            double descuento = t.obtenerPorcentajeDescuento(new ArrayList<>());

            System.out.printf("%-25s | Descuento: %5.1f%% | Monto: $%.2f%n",
                    bono.getNombre(), descuento * 100, monto);
        }
    }
}
