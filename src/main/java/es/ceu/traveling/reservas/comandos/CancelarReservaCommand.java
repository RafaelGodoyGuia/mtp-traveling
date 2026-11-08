package es.ceu.traveling.reservas.comandos;

import es.ceu.traveling.reservas.Reserva;
import java.util.Objects;

public final class CancelarReservaCommand implements Comando {
    private final Reserva reserva;
    public CancelarReservaCommand(Reserva reserva) { this.reserva = Objects.requireNonNull(reserva, "reserva"); }
    @Override public void ejecutar() { reserva.cancelar(); }
}
