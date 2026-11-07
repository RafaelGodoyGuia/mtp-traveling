package es.ceu.traveling.servicios;

import java.math.BigDecimal;
import java.util.Objects;

public class CocheAlquiler extends ServicioSimple {
    private final String modelo;
    private final int dias;
    private final BigDecimal precioDia;

    public CocheAlquiler(String modelo, String ciudad, int dias, BigDecimal precioDia) {
        super(ciudad);
        if (dias < 1) {
            throw new IllegalArgumentException("Un coche se alquila al menos un día");
        }
        this.modelo = Objects.requireNonNull(modelo, "modelo");
        this.dias = dias;
        this.precioDia = noNegativo(precioDia);
    }

    @Override
    public String getNombre() {
        return "Coche " + modelo + " en " + getDestino() + ", " + dias + " días";
    }

    @Override
    public BigDecimal calcularPrecio() {
        return precioDia.multiply(BigDecimal.valueOf(dias));
    }
}
