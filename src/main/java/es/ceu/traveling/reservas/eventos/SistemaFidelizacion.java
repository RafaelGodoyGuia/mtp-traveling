package es.ceu.traveling.reservas.eventos;

import java.util.HashMap;
import java.util.Map;

/** Observador que suma puntos cuando una reserva queda confirmada. */
public class SistemaFidelizacion implements ObservadorReserva {
    private final Map<String, Integer> puntosPorEmail = new HashMap<>();
    private final int puntosPorConfirmacion;
    public SistemaFidelizacion() { this(10); }
    public SistemaFidelizacion(int puntosPorConfirmacion) {
        if (puntosPorConfirmacion < 0) throw new IllegalArgumentException("Los puntos no pueden ser negativos");
        this.puntosPorConfirmacion = puntosPorConfirmacion;
    }
    @Override public void actualizar(EventoReserva e) {
        if (e.tipo() == TipoEventoReserva.CONFIRMADA) {
            String email = e.reserva().getCliente().email();
            puntosPorEmail.merge(email, puntosPorConfirmacion, Integer::sum);
        }
    }
    public int consultarPuntos(String email) { return puntosPorEmail.getOrDefault(email, 0); }
}
