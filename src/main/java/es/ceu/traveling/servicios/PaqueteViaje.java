package es.ceu.traveling.servicios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Composite: un paquete contiene servicios simples u otros paquetes, con anidamiento arbitrario. */
public class PaqueteViaje implements ServicioViaje {
    private final String nombre;
    private final String destino;
    private final List<ServicioViaje> hijos = new ArrayList<>();

    public PaqueteViaje(String nombre, String destino) {
        this.nombre = Objects.requireNonNull(nombre, "nombre");
        this.destino = Objects.requireNonNull(destino, "destino");
    }

    public PaqueteViaje agregar(ServicioViaje hijo) {
        Objects.requireNonNull(hijo, "hijo");
        // Un paquete dentro de sí mismo haría infinitas calcularPrecio y mostrar.
        if (hijo == this || (hijo instanceof PaqueteViaje p && p.contiene(this))) {
            throw new IllegalArgumentException("Un paquete no puede contenerse a sí mismo");
        }
        hijos.add(hijo);
        return this;
    }

    public boolean quitar(ServicioViaje hijo) {
        return hijos.remove(hijo);
    }

    public List<ServicioViaje> getHijos() {
        return List.copyOf(hijos);
    }

    /** Si {@code buscado} está en este paquete a cualquier profundidad. */
    public boolean contiene(ServicioViaje buscado) {
        return hijos.stream().anyMatch(h -> h == buscado || (h instanceof PaqueteViaje p && p.contiene(buscado)));
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDestino() {
        return destino;
    }

    @Override
    public BigDecimal calcularPrecio() {
        return hijos.stream().map(ServicioViaje::calcularPrecio).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String mostrar(String sangria) {
        StringBuilder sb = new StringBuilder(sangria).append("+ Paquete ").append(nombre)
                .append(": ").append(calcularPrecio()).append(" €").append(isCancelado() ? " [CANCELADO]" : "");
        for (ServicioViaje hijo : hijos) {
            sb.append('\n').append(hijo.mostrar(sangria + "    "));
        }
        return sb.toString();
    }

    @Override
    public void cancelar() {
        hijos.forEach(ServicioViaje::cancelar);
    }

    /** Un paquete está cancelado cuando lo están todos sus servicios; vacío no cuenta como cancelado. */
    @Override
    public boolean isCancelado() {
        return !hijos.isEmpty() && hijos.stream().allMatch(ServicioViaje::isCancelado);
    }
}
