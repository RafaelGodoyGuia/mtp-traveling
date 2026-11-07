package es.ceu.traveling;

import es.ceu.traveling.servicios.PaqueteViaje;
import es.ceu.traveling.servicios.ServicioViaje;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static es.ceu.traveling.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class PaqueteViajeTest {
    @Test
    void precioTotalSumaTodosLosNiveles() {
        // 120 + 4*95 + 4*40 + (80 + 2*110)
        assertEquals(new BigDecimal("960.00"), inglaterra().calcularPrecio());
    }

    @Test
    void mostrarSangraCadaNivel() {
        String[] lineas = inglaterra().mostrar().split("\n");
        assertEquals(7, lineas.length);
        assertTrue(lineas[0].startsWith("+ Paquete Viaje a Inglaterra"));
        assertTrue(lineas[4].startsWith("    + Paquete Extensión a París"));
        assertTrue(lineas[5].startsWith("        - Vuelo AF1381"));
    }

    @Test
    void cancelarElPaqueteCancelaTodoElArbol() {
        PaqueteViaje p = inglaterra();
        p.cancelar();
        assertTrue(p.isCancelado());
        PaqueteViaje paris = (PaqueteViaje) p.getHijos().get(3);
        assertTrue(paris.getHijos().stream().allMatch(ServicioViaje::isCancelado));
    }

    @Test
    void cancelarUnServicioNoCancelaElPaquete() {
        PaqueteViaje p = inglaterra();
        p.getHijos().get(0).cancelar();
        assertFalse(p.isCancelado());
    }

    @Test
    void servicioSimpleYPaqueteSeUsanIgual() {
        for (ServicioViaje s : new ServicioViaje[] {vuelo(), inglaterra()}) {
            assertTrue(s.calcularPrecio().signum() > 0);
            assertFalse(s.mostrar().isBlank());
        }
    }

    @Test
    void rechazaCiclos() {
        PaqueteViaje exterior = new PaqueteViaje("A", "X");
        PaqueteViaje interior = new PaqueteViaje("B", "X");
        exterior.agregar(interior);
        assertThrows(IllegalArgumentException.class, () -> exterior.agregar(exterior));
        assertThrows(IllegalArgumentException.class, () -> interior.agregar(exterior));
    }

    @Test
    void paqueteVacioNoEstaCanceladoYCuestaCero() {
        PaqueteViaje vacio = new PaqueteViaje("Vacío", "X");
        assertFalse(vacio.isCancelado());
        assertEquals(BigDecimal.ZERO, vacio.calcularPrecio());
    }
}
