package es.ceu.traveling.adaptadores;

import es.ceu.traveling.servicios.ServicioViaje;
// Assume HotelExternoAPI is a legacy class provided by a third party
import com.terceros.api.HotelExternoAPI;

public class HotelExternoAdapter implements ServicioViaje {
    private HotelExternoAPI apiExterna;
    private String fechaReserva;

    public HotelExternoAdapter(HotelExternoAPI apiExterna, String fechaReserva) {
        this.apiExterna = apiExterna;
        this.fechaReserva = fechaReserva;
    }

    @Override
    public double getPrecio() {
        // Translating the external API's logic to our standard interface
        return apiExterna.findRooms(fechaReserva).getBaseRate();
    }

    @Override
    public String getDescripcion() {
        return "Hotel adaptado desde API externa: " + apiExterna.getHotelName();
    }
}
