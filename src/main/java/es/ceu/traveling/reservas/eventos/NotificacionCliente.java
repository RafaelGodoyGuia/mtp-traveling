package es.ceu.traveling.reservas.eventos;

import java.util.ArrayList;
import java.util.List;

/** Observador de ejemplo que conserva las notificaciones generadas para el cliente. */
public class NotificacionCliente implements ObservadorReserva {
    private final List<String> notificaciones = new ArrayList<>();
    @Override public void actualizar(EventoReserva e) {
        notificaciones.add("Reserva " + e.reserva().getId() + ": estado " + e.estadoNuevo());
    }
    public List<String> getNotificaciones() { return List.copyOf(notificaciones); }
}
