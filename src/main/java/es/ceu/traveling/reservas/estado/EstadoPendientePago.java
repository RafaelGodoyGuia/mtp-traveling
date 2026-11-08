package es.ceu.traveling.reservas.estado;

import es.ceu.traveling.reservas.Reserva;

public final class EstadoPendientePago implements EstadoReserva {
    @Override public String nombre() { return "Pendiente de pago"; }
    @Override public void pasarAPendientePago(Reserva r) { throw noPermitida("repetir el paso a pendiente de pago"); }
    @Override public void confirmar(Reserva r) { r.cambiarEstadoInterno(new EstadoConfirmada()); }
    @Override public void completar(Reserva r) { throw noPermitida("completar"); }
    @Override public void cancelar(Reserva r) { r.cancelarServicioInterno(); r.cambiarEstadoInterno(new EstadoCancelada()); }
    private IllegalStateException noPermitida(String a) { return new IllegalStateException("No se puede " + a + " una reserva Pendiente de pago"); }
}
