package es.ceu.traveling.reservas.estado;

import es.ceu.traveling.reservas.Reserva;

public final class EstadoCreada implements EstadoReserva {
    @Override public String nombre() { return "Creada"; }
    @Override public void pasarAPendientePago(Reserva r) { r.cambiarEstadoInterno(new EstadoPendientePago()); }
    @Override public void confirmar(Reserva r) { r.cambiarEstadoInterno(new EstadoConfirmada()); }
    @Override public void completar(Reserva r) { throw noPermitida("completar"); }
    @Override public void cancelar(Reserva r) { r.cancelarServicioInterno(); r.cambiarEstadoInterno(new EstadoCancelada()); }
    private IllegalStateException noPermitida(String accion) { return new IllegalStateException("No se puede " + accion + " una reserva Creada"); }
}
