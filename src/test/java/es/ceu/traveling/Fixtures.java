package es.ceu.traveling;

import es.ceu.traveling.dominio.Cliente;
import es.ceu.traveling.servicios.CocheAlquiler;
import es.ceu.traveling.servicios.Hotel;
import es.ceu.traveling.servicios.PaqueteViaje;
import es.ceu.traveling.servicios.Vuelo;

import java.math.BigDecimal;
import java.time.LocalDate;

final class Fixtures {
    static final LocalDate IDA = LocalDate.of(2027, 3, 1);
    static final Cliente ANA = new Cliente("Ana López", "ana@example.com");

    static Vuelo vuelo() { return new Vuelo("Madrid", "Londres", "IB3166", IDA, new BigDecimal("120.00")); }
    static Hotel hotel() { return new Hotel("Strand Palace", "Londres", 4, new BigDecimal("95.00")); }
    static CocheAlquiler coche() { return new CocheAlquiler("Mini", "Londres", 4, new BigDecimal("40.00")); }

    /** El ejemplo del enunciado: Inglaterra (vuelo, hotel, coche) con una extensión a París (vuelo, hotel). */
    static PaqueteViaje inglaterra() {
        PaqueteViaje paris = new PaqueteViaje("Extensión a París", "París")
                .agregar(new Vuelo("Londres", "París", "AF1381", IDA.plusDays(4), new BigDecimal("80.00")))
                .agregar(new Hotel("Le Marais", "París", 2, new BigDecimal("110.00")));
        return new PaqueteViaje("Viaje a Inglaterra", "Londres")
                .agregar(vuelo()).agregar(hotel()).agregar(coche()).agregar(paris);
    }

    private Fixtures() {}
}
