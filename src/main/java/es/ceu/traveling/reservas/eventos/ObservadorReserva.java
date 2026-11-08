package es.ceu.traveling.reservas.eventos;

/** Observer: cualquier componente interesado en los cambios de una reserva implementa esta interfaz. */
@FunctionalInterface
public interface ObservadorReserva {
    void actualizar(EventoReserva evento);
}
