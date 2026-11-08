package es.ceu.traveling.reservas.estado;

import es.ceu.traveling.reservas.Reserva;

public final class EstadoCancelada implements EstadoReserva {
    @Override public String nombre() { return "Cancelada"; }
    @Override public void pasarAPendientePago(Reserva r) { throw noPermitida("reactivar"); }
    @Override public void confirmar(Reserva r) { throw noPermitida("confirmar"); }
    @Override public void completar(Reserva r) { throw noPermitida("completar"); }
    @Override public void cancelar(Reserva r) { throw noPermitida("cancelar de nuevo"); }
    private IllegalStateException noPermitida(String a) { return new IllegalStateException("No se puede " + a + " una reserva Cancelada"); }
}
