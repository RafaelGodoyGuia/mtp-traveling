package es.ceu.traveling.servicios;

import java.math.BigDecimal;
import java.util.Objects;

public class Hotel extends ServicioSimple {
    private final String nombre;
    private final int noches;
    private final BigDecimal precioNoche;

    public Hotel(String nombre, String ciudad, int noches, BigDecimal precioNoche) {
        super(ciudad);
        if (noches < 1) {
            throw new IllegalArgumentException("Un hotel se reserva al menos una noche");
        }
        this.nombre = Objects.requireNonNull(nombre, "nombre");
        this.noches = noches;
        this.precioNoche = noNegativo(precioNoche);
    }

    @Override
    public String getNombre() {
        return "Hotel " + nombre + " en " + getDestino() + ", " + noches + " noches";
    }

    @Override
    public BigDecimal calcularPrecio() {
        return precioNoche.multiply(BigDecimal.valueOf(noches));
    }
}
