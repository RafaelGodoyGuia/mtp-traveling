package es.ceu.traveling.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Vuelo extends ServicioSimple {
    private final String origen;
    private final String numeroVuelo;
    private final LocalDate fecha;
    private final BigDecimal precio;

    public Vuelo(String origen, String destino, String numeroVuelo, LocalDate fecha, BigDecimal precio) {
        super(destino);
        this.origen = Objects.requireNonNull(origen, "origen");
        this.numeroVuelo = Objects.requireNonNull(numeroVuelo, "numeroVuelo");
        this.fecha = Objects.requireNonNull(fecha, "fecha");
        this.precio = noNegativo(precio);
    }

    @Override
    public String getNombre() {
        return "Vuelo " + numeroVuelo + " " + origen + " → " + getDestino() + " (" + fecha + ")";
    }

    @Override
    public BigDecimal calcularPrecio() {
        return precio;
    }
}
