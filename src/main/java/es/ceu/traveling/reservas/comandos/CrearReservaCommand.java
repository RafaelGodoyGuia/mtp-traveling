package es.ceu.traveling.reservas.comandos;

import es.ceu.traveling.reservas.GestorReservas;
import es.ceu.traveling.reservas.Reserva;
import java.util.Objects;

public final class CrearReservaCommand implements Comando {
    private final GestorReservas gestor;
    private final Reserva reserva;
    public CrearReservaCommand(GestorReservas gestor, Reserva reserva) {
        this.gestor = Objects.requireNonNull(gestor, "gestor");
        this.reserva = Objects.requireNonNull(reserva, "reserva");
    }
    @Override public void ejecutar() { gestor.crear(reserva); }
    public Reserva getReserva() { return reserva; }
}
