package es.ceu.traveling.servicios;

import java.math.BigDecimal;

/**
 * Contrato común de todo lo que se puede reservar (Composite: componente).
 * Lo cumplen igual un servicio simple (vuelo, hotel, coche) y un paquete con otros servicios dentro.
 */
public interface ServicioViaje {
    String getNombre();

    String getDestino();

    BigDecimal calcularPrecio();

    /** Contenido del servicio, una línea por elemento, sangrado según la profundidad. */
    String mostrar(String sangria);

    default String mostrar() {
        return mostrar("");
    }

    void cancelar();

    boolean isCancelado();
}
