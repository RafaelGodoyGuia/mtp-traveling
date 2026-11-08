package es.ceu.traveling.reservas.comandos;

import es.ceu.traveling.reservas.Reserva;
import java.util.Objects;

public final class CompletarReservaCommand implements Comando {
    private final Reserva reserva;
    public CompletarReservaCommand(Reserva reserva) { this.reserva = Objects.requireNonNull(reserva, "reserva"); }
    @Override public void ejecutar() { reserva.completar(); }
}
