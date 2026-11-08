package es.ceu.traveling.reservas.eventos;

/** Eventos publicados por una reserva cuando cambia su ciclo de vida. */
public enum TipoEventoReserva {
    CREADA, PENDIENTE_PAGO, CONFIRMADA, COMPLETADA, CANCELADA
}
