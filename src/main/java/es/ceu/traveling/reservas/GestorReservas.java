package es.ceu.traveling.reservas;

import es.ceu.traveling.servicios.Proveedor;
import es.ceu.traveling.servicios.ServicioViaje;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/** Casos de uso del cliente: buscar servicios, crear, consultar y cancelar reservas. */
public class GestorReservas {
    private final List<Proveedor> proveedores;
    private final Map<String, Reserva> reservas = new LinkedHashMap<>();

    public GestorReservas(List<Proveedor> proveedores) {
        this.proveedores = List.copyOf(proveedores);
    }

    public List<ServicioViaje> buscar(String destino) {
        return proveedores.stream().flatMap(p -> p.buscar(destino).stream()).toList();
    }

    public Reserva crear(Reserva reserva) {
        reservas.put(Objects.requireNonNull(reserva, "reserva").getId(), reserva);
        return reserva;
    }

    public Optional<Reserva> consultar(String id) {
        return Optional.ofNullable(reservas.get(id));
    }

    public void cancelar(String id) {
        consultar(id).orElseThrow(() -> new NoSuchElementException("No existe la reserva " + id)).cancelar();
    }
}
