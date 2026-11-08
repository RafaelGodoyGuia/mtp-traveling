package es.ceu.traveling.reservas.estado;

import es.ceu.traveling.reservas.Reserva;

/** State: define las operaciones permitidas según el estado actual de una reserva. */
public interface EstadoReserva {
    String nombre();
    void pasarAPendientePago(Reserva reserva);
    void confirmar(Reserva reserva);
    void completar(Reserva reserva);
    void cancelar(Reserva reserva);
}
