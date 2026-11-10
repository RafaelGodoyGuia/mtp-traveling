package es.ceu.traveling.adaptadores;

import es.ceu.traveling.servicios.ServicioViaje;
import es.ceu.traveling.externos.HotelExternoAPI; // Ahora importa tu propia clase simulada
import java.math.BigDecimal;

public class HotelExternoAdapter implements ServicioViaje {
    private HotelExternoAPI apiExterna;
    private String fechaReserva;
    private boolean cancelado;

    public HotelExternoAdapter(HotelExternoAPI apiExterna, String fechaReserva) {
        this.apiExterna = apiExterna;
        this.fechaReserva = fechaReserva;
        this.cancelado = false;
    }

    @Override
    public String getNombre() {
        return "Hotel adaptado: " + apiExterna.getHotelName();
    }

    @Override
    public String getDestino() {
        return "Destino gestionado por proveedor externo";
    }

    @Override
    public BigDecimal calcularPrecio() {
        // Usamos la API simulada y su método findRooms()
        return BigDecimal.valueOf(apiExterna.findRooms(fechaReserva).getBaseRate());
    }

    @Override
    public String mostrar(String sangria) {
        return sangria + "- " + getNombre() + " (Fecha: " + fechaReserva + ") - Precio: " + calcularPrecio();
    }

    @Override
    public void cancelar() {
        this.cancelado = true;
    }

    @Override
    public boolean isCancelado() {
        return this.cancelado;
    }
}
