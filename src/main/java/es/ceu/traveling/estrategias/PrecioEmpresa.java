package es.ceu.traveling.estrategias;

public class PrecioEmpresas implements EstrategiaPrecio {
    private static final double DESCUENTO_CORPORATIVO = 0.85; // 15% discount

    @Override
    public double calcularPrecioFinal(double precioBase) {
        return precioBase * DESCUENTO_CORPORATIVO;
    }
}
