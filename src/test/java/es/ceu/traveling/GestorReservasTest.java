package es.ceu.traveling;

import es.ceu.traveling.reservas.GestorReservas;
import es.ceu.traveling.reservas.Reserva;
import es.ceu.traveling.servicios.ServicioViaje;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.NoSuchElementException;

import static es.ceu.traveling.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

/** Caso de uso completo (buscar → crear → consultar → cancelar) para cada tipo de servicio. */
class GestorReservasTest {
    private final GestorReservas gestor = new GestorReservas(List.of(
            d -> List.of(vuelo()), d -> List.of(hotel()), d -> List.of(coche())));

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2}) // vuelo, hotel, coche
    void casoDeUsoCompleto(int tipo) {
        List<ServicioViaje> encontrados = gestor.buscar("Londres");
        assertEquals(3, encontrados.size());

        Reserva r = gestor.crear(Reserva.builder(ANA, encontrados.get(tipo)).fechas(IDA, IDA.plusDays(4)).build());
        assertSame(r, gestor.consultar(r.getId()).orElseThrow());
        assertFalse(r.isCancelada());

        gestor.cancelar(r.getId());
        assertTrue(gestor.consultar(r.getId()).orElseThrow().isCancelada());
    }

    @Test
    void reservaDePaquete() {
        Reserva r = gestor.crear(Reserva.builder(ANA, inglaterra()).build());
        gestor.cancelar(r.getId());
        assertTrue(r.isCancelada());
    }

    @Test
    void consultarOCancelarUnaReservaQueNoExiste() {
        assertTrue(gestor.consultar("nada").isEmpty());
        assertThrows(NoSuchElementException.class, () -> gestor.cancelar("nada"));
    }
}
