package es.ceu.traveling.decoradores;

import es.ceu.traveling.servicios.ServicioViaje;

public abstract class ServicioAdicionalDecorator implements ServicioViaje {
    protected ServicioViaje servicioEnvuelto;

    public ServicioAdicionalDecorator(ServicioViaje servicio) {
        this.servicioEnvuelto = servicio;
    }

    @Override
    public double getPrecio() {
        return servicioEnvuelto.getPrecio();
    }

    @Override
    public String getDescripcion() {
        return servicioEnvuelto.getDescripcion();
    }
}
