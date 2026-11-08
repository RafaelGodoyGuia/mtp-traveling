package es.ceu.traveling.estrategias;

public class PrecioFidelizacion implements EstrategiaPrecio {
    private static final double DESCUENTO_FIEL = 0.75; // 25% descuento

    @Override
    public double calcularPrecioFinal(double precioBase) {
        return precioBase * DESCUENTO_FIEL;
    }
}
