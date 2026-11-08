package es.ceu.traveling.reservas.eventos;

import es.ceu.traveling.reservas.Reserva;
import java.time.Instant;
import java.util.Objects;

public record EventoReserva(Reserva reserva, TipoEventoReserva tipo, String estadoAnterior,
                            String estadoNuevo, Instant fecha) {
    public EventoReserva {
        Objects.requireNonNull(reserva, "reserva");
        Objects.requireNonNull(tipo, "tipo");
        Objects.requireNonNull(estadoNuevo, "estadoNuevo");
        Objects.requireNonNull(fecha, "fecha");
    }
}
