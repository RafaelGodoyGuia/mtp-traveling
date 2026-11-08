package es.ceu.traveling.decoradores;

import es.ceu.traveling.servicios.ServicioViaje;

public class SeguroViajeDecorator extends ServicioAdicionalDecorator {
    private double costoSeguro;

    public SeguroViajeDecorator(ServicioViaje servicio, double costoSeguro) {
        super(servicio);
        this.costoSeguro = costoSeguro;
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + costoSeguro;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Seguro de Viaje";
    }
}
