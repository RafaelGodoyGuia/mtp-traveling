package es.ceu.traveling.reservas.comandos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/** Invocador Command: permite ejecutar ahora o encolar comandos para ejecutarlos después. */
public class EjecutorComandos {
    private final Deque<Comando> cola = new ArrayDeque<>();
    private final List<Comando> historial = new ArrayList<>();

    public void ejecutar(Comando comando) {
        Objects.requireNonNull(comando, "comando").ejecutar();
        historial.add(comando);
    }
    public void encolar(Comando comando) { cola.addLast(Objects.requireNonNull(comando, "comando")); }
    public boolean ejecutarSiguiente() {
        Comando comando = cola.pollFirst();
        if (comando == null) return false;
        ejecutar(comando);
        return true;
    }
    public void ejecutarTodos() { while (ejecutarSiguiente()) { /* procesa la cola */ } }
    public int comandosPendientes() { return cola.size(); }
    public List<Comando> getHistorial() { return List.copyOf(historial); }
}
