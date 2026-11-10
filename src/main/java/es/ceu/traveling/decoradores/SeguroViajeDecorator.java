package es.ceu.traveling.decoradores;

import java.math.BigDecimal;

import es.ceu.traveling.servicios.ServicioViaje;

public class SeguroViajeDecorator extends ServicioAdicionalDecorator {
    private BigDecimal costoSeguro;

    public SeguroViajeDecorator(ServicioViaje servicio, BigDecimal costoSeguro) {
        super(servicio);
        this.costoSeguro = costoSeguro;
    }

    @Override
    public BigDecimal calcularPrecio() {
        return super.calcularPrecio().add(costoSeguro);
    }

    @Override
    public String mostrar(String sangria) {
        return super.mostrar() + " + Seguro de Viaje";
    }

    @Override
    public String getDestino(){
        return super.getDestino();
    }

    @Override
    public void cancelar(){
        super.cancelar();
    }

    @Override
    public boolean isCancelado(){
        return super.isCancelado();
    }

    @Override
    public String getNombre(){
        return super.getNombre();
    }
}
