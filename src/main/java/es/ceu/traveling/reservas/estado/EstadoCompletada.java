package es.ceu.traveling.reservas.estado;

import es.ceu.traveling.reservas.Reserva;

public final class EstadoCompletada implements EstadoReserva {
    @Override public String nombre() { return "Completada"; }
    @Override public void pasarAPendientePago(Reserva r) { throw noPermitida("volver a pendiente de pago"); }
    @Override public void confirmar(Reserva r) { throw noPermitida("confirmar"); }
    @Override public void completar(Reserva r) { throw noPermitida("completar de nuevo"); }
    @Override public void cancelar(Reserva r) { throw noPermitida("cancelar"); }
    private IllegalStateException noPermitida(String a) { return new IllegalStateException("No se puede " + a + " una reserva Completada"); }
}
