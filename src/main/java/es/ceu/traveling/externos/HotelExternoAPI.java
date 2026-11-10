package es.ceu.traveling.externos;

/**
 * Esta clase simula la API de un proveedor externo.
 * Tiene métodos incompatibles con nuestra interfaz ServicioViaje.
 */
public class HotelExternoAPI {
    private String nombreHotel;
    private double tarifaBase;

    public HotelExternoAPI(String nombreHotel, double tarifaBase) {
        this.nombreHotel = nombreHotel;
        this.tarifaBase = tarifaBase;
    }

    public String getHotelName() {
        return nombreHotel;
    }

    // El método incompatible que menciona la rúbrica de la Fase 1
    public HotelExternoAPI findRooms(String date) {
        // Aquí iría la lógica del proveedor, devolvemos 'this' a modo de simulación
        return this;
    }

    public double getBaseRate() {
        return tarifaBase;
    }
}
