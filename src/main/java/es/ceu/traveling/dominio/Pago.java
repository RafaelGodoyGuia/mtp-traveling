package es.ceu.traveling.dominio;

import java.util.Objects;

/** Información de pago de una reserva. No guarda datos de tarjeta: solo el método y el titular. */
public record Pago(String metodo, String titular) {
    public Pago {
        Objects.requireNonNull(metodo, "metodo");
        Objects.requireNonNull(titular, "titular");
    }
}
