package es.ceu.traveling.servicios;

import java.util.List;

/**
 * Lo que la plataforma espera de cualquier fuente de servicios. Los proveedores externos
 * (aerolínea, cadena hotelera, alquiler de coches) se conectan con un Adapter que implementa esta interfaz.
 */
@FunctionalInterface
public interface Proveedor {
    List<ServicioViaje> buscar(String destino);
}
