package es.ceu.traveling.reservas;

import es.ceu.traveling.dominio.Cliente;
import es.ceu.traveling.dominio.Pago;
import es.ceu.traveling.servicios.ServicioViaje;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Reserva de un servicio (simple o paquete). Inmutable salvo la cancelación;
 * se construye solo con {@link Builder}, que evita el constructor telescópico.
 */
public final class Reserva {
    private final String id = UUID.randomUUID().toString();
    private final Cliente cliente;
    private final ServicioViaje servicio;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final String destino;
    private final String preferencias;
    private final int equipaje;
    private final List<String> solicitudesEspeciales;
    private final String seguro;
    private final Pago pago;

    private Reserva(Builder b) {
        cliente = b.cliente;
        servicio = b.servicio;
        fechaInicio = b.fechaInicio;
        fechaFin = b.fechaFin;
        destino = b.destino != null ? b.destino : b.servicio.getDestino();
        preferencias = b.preferencias;
        equipaje = b.equipaje;
        solicitudesEspeciales = List.copyOf(b.solicitudesEspeciales);
        seguro = b.seguro;
        pago = b.pago;
    }

    /** Cliente y servicio son obligatorios; todo lo demás se añade paso a paso. */
    public static Builder builder(Cliente cliente, ServicioViaje servicio) {
        return new Builder(cliente, servicio);
    }

    public String getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public ServicioViaje getServicio() { return servicio; }
    public String getDestino() { return destino; }
    public Optional<LocalDate> getFechaInicio() { return Optional.ofNullable(fechaInicio); }
    public Optional<LocalDate> getFechaFin() { return Optional.ofNullable(fechaFin); }
    public Optional<String> getPreferencias() { return Optional.ofNullable(preferencias); }
    public int getEquipaje() { return equipaje; }
    public List<String> getSolicitudesEspeciales() { return solicitudesEspeciales; }
    public Optional<String> getSeguro() { return Optional.ofNullable(seguro); }
    public Optional<Pago> getPago() { return Optional.ofNullable(pago); }

    public BigDecimal calcularPrecio() {
        return servicio.calcularPrecio();
    }

    public void cancelar() {
        servicio.cancelar();
    }

    public boolean isCancelada() {
        return servicio.isCancelado();
    }

    public String mostrar() {
        return "Reserva " + id + " de " + cliente.nombre() + " a " + destino + " — " + calcularPrecio() + " €\n"
                + servicio.mostrar("  ");
    }

    public static final class Builder {
        private final Cliente cliente;
        private final ServicioViaje servicio;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private String destino;
        private String preferencias;
        private int equipaje;
        private final List<String> solicitudesEspeciales = new ArrayList<>();
        private String seguro;
        private Pago pago;

        private Builder(Cliente cliente, ServicioViaje servicio) {
            this.cliente = Objects.requireNonNull(cliente, "cliente");
            this.servicio = Objects.requireNonNull(servicio, "servicio");
        }

        public Builder fechas(LocalDate inicio, LocalDate fin) {
            this.fechaInicio = Objects.requireNonNull(inicio, "inicio");
            this.fechaFin = Objects.requireNonNull(fin, "fin");
            return this;
        }

        public Builder destino(String destino) {
            this.destino = Objects.requireNonNull(destino, "destino");
            return this;
        }

        /** Asiento en un vuelo, tipo de habitación en un hotel… */
        public Builder preferencias(String preferencias) {
            this.preferencias = Objects.requireNonNull(preferencias, "preferencias");
            return this;
        }

        public Builder equipaje(int bultos) {
            if (bultos < 0) {
                throw new IllegalArgumentException("El equipaje no puede ser negativo");
            }
            this.equipaje = bultos;
            return this;
        }

        public Builder solicitudEspecial(String solicitud) {
            solicitudesEspeciales.add(Objects.requireNonNull(solicitud, "solicitud"));
            return this;
        }

        public Builder seguro(String poliza) {
            this.seguro = Objects.requireNonNull(poliza, "poliza");
            return this;
        }

        public Builder pago(Pago pago) {
            this.pago = Objects.requireNonNull(pago, "pago");
            return this;
        }

        /** Valida la combinación completa: aquí, y no en cada paso, es donde se conocen todos los datos. */
        public Reserva build() {
            if (fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
                throw new IllegalStateException("La fecha de fin es anterior a la de inicio");
            }
            return new Reserva(this);
        }
    }
}
