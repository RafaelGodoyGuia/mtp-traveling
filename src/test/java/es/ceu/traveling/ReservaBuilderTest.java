package es.ceu.traveling;

import es.ceu.traveling.dominio.Pago;
import es.ceu.traveling.reservas.Reserva;
import org.junit.jupiter.api.Test;

import java.util.List;

import static es.ceu.traveling.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class ReservaBuilderTest {
    @Test
    void reservaMinimaSoloConObligatorios() {
        Reserva r = Reserva.builder(ANA, vuelo()).build();
        assertEquals("Londres", r.getDestino(), "el destino sale del servicio si no se da");
        assertTrue(r.getFechaInicio().isEmpty());
        assertTrue(r.getPago().isEmpty());
        assertEquals(0, r.getEquipaje());
    }

    @Test
    void reservaCompletaPasoAPaso() {
        Reserva r = Reserva.builder(ANA, hotel())
                .fechas(IDA, IDA.plusDays(4))
                .destino("Londres centro")
                .preferencias("Habitación doble")
                .equipaje(2)
                .solicitudEspecial("Cuna")
                .solicitudEspecial("Llegada tarde")
                .seguro("Póliza básica")
                .pago(new Pago("Tarjeta", "Ana López"))
                .build();
        assertEquals("Londres centro", r.getDestino());
        assertEquals(IDA.plusDays(4), r.getFechaFin().orElseThrow());
        assertEquals(List.of("Cuna", "Llegada tarde"), r.getSolicitudesEspeciales());
        assertEquals("Tarjeta", r.getPago().orElseThrow().metodo());
    }

    @Test
    void rechazaFechasInvertidas() {
        var b = Reserva.builder(ANA, coche()).fechas(IDA, IDA.minusDays(1));
        assertThrows(IllegalStateException.class, b::build);
    }

    @Test
    void rechazaObligatoriosNulosYEquipajeNegativo() {
        assertThrows(NullPointerException.class, () -> Reserva.builder(null, vuelo()));
        assertThrows(NullPointerException.class, () -> Reserva.builder(ANA, null));
        assertThrows(IllegalArgumentException.class, () -> Reserva.builder(ANA, vuelo()).equipaje(-1));
    }
}
