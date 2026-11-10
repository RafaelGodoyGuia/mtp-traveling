package es.ceu.traveling.decoradores;

import java.math.BigDecimal;

import es.ceu.traveling.servicios.ServicioViaje;

public abstract class ServicioAdicionalDecorator implements ServicioViaje {
    protected ServicioViaje servicioEnvuelto;

    public ServicioAdicionalDecorator(ServicioViaje servicio) {
        this.servicioEnvuelto = servicio;
    }

    @Override
    public BigDecimal calcularPrecio() {
        return servicioEnvuelto.calcularPrecio();
    }

    @Override
    public String mostrar() {
        return servicioEnvuelto.mostrar();
    }

    @Override
    public String getDestino(){
        return servicioEnvuelto.getDestino();
    }

    @Override
    public void cancelar(){
        servicioEnvuelto.cancelar();
    }

    @Override
    public boolean isCancelado(){
        return servicioEnvuelto.isCancelado();
    }

    @Override
    public String getNombre(){
        return servicioEnvuelto.getNombre();
    }
}
