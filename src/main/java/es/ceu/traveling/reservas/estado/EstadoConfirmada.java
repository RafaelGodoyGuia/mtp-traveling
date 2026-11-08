package es.ceu.traveling.reservas.estado;

import es.ceu.traveling.reservas.Reserva;

public final class EstadoConfirmada implements EstadoReserva {
    @Override public String nombre() { return "Confirmada"; }
    @Override public void pasarAPendientePago(Reserva r) { throw noPermitida("volver a pendiente de pago"); }
    @Override public void confirmar(Reserva r) { throw noPermitida("confirmar de nuevo"); }
    @Override public void completar(Reserva r) { r.cambiarEstadoInterno(new EstadoCompletada()); }
    @Override public void cancelar(Reserva r) { r.cancelarServicioInterno(); r.cambiarEstadoInterno(new EstadoCancelada()); }
    private IllegalStateException noPermitida(String a) { return new IllegalStateException("No se puede " + a + " una reserva Confirmada"); }
}
