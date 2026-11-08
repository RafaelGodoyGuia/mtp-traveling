package es.ceu.traveling.reservas.eventos;

import java.util.EnumMap;
import java.util.Map;

/** Observador que contabiliza los eventos del ciclo de vida. */
public class SistemaEstadisticas implements ObservadorReserva {
    private final Map<TipoEventoReserva, Long> contador = new EnumMap<>(TipoEventoReserva.class);
    @Override public void actualizar(EventoReserva e) { contador.merge(e.tipo(), 1L, Long::sum); }
    public long consultar(TipoEventoReserva tipo) { return contador.getOrDefault(tipo, 0L); }
    public Map<TipoEventoReserva, Long> obtenerResumen() { return Map.copyOf(contador); }
}
