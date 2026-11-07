package es.ceu.traveling.servicios;

import java.math.BigDecimal;
import java.util.Objects;

/** Base de las hojas del Composite: un servicio que no contiene otros. */
public abstract class ServicioSimple implements ServicioViaje {
    private final String destino;
    private boolean cancelado;

    protected ServicioSimple(String destino) {
        this.destino = Objects.requireNonNull(destino, "destino");
    }

    protected static BigDecimal noNegativo(BigDecimal importe) {
        if (Objects.requireNonNull(importe, "importe").signum() < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo: " + importe);
        }
        return importe;
    }

    @Override
    public String getDestino() {
        return destino;
    }

    @Override
    public String mostrar(String sangria) {
        return sangria + "- " + getNombre() + ": " + calcularPrecio() + " €" + (cancelado ? " [CANCELADO]" : "");
    }

    @Override
    public void cancelar() {
        cancelado = true;
    }

    @Override
    public boolean isCancelado() {
        return cancelado;
    }
}
