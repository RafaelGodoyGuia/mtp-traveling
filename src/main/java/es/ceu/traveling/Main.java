package es.ceu.traveling;

import es.ceu.traveling.dominio.Cliente;
import es.ceu.traveling.dominio.Pago;
import es.ceu.traveling.reservas.GestorReservas;
import es.ceu.traveling.reservas.Reserva;
import es.ceu.traveling.servicios.CocheAlquiler;
import es.ceu.traveling.servicios.Hotel;
import es.ceu.traveling.servicios.PaqueteViaje;
import es.ceu.traveling.servicios.ServicioViaje;
import es.ceu.traveling.servicios.Vuelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Demostración por consola de los casos de uso de la Fase 1 (Builder + Composite). */
public class Main {
    public static void main(String[] args) {
        LocalDate ida = LocalDate.of(2027, 3, 1);
        GestorReservas gestor = new GestorReservas(List.of(
                destino -> List.of(new Vuelo("Madrid", destino, "IB3166", ida, new BigDecimal("120.00"))),
                destino -> List.of(new Hotel("Strand Palace", destino, 4, new BigDecimal("95.00"))),
                destino -> List.of(new CocheAlquiler("Mini Cooper", destino, 4, new BigDecimal("40.00")))));
        Cliente ana = new Cliente("Ana López", "ana@example.com");

        System.out.println("== Búsqueda en Londres ==");
        List<ServicioViaje> encontrados = gestor.buscar("Londres");
        encontrados.forEach(s -> System.out.println(s.mostrar()));

        System.out.println("\n== Una reserva por servicio ==");
        for (ServicioViaje s : encontrados) {
            Reserva r = gestor.crear(Reserva.builder(ana, s).fechas(ida, ida.plusDays(4)).build());
            System.out.println(gestor.consultar(r.getId()).orElseThrow().mostrar());
        }

        System.out.println("\n== Paquete jerárquico ==");
        PaqueteViaje paris = new PaqueteViaje("Extensión a París", "París")
                .agregar(new Vuelo("Londres", "París", "AF1381", ida.plusDays(4), new BigDecimal("80.00")))
                .agregar(new Hotel("Le Marais", "París", 2, new BigDecimal("110.00")));
        PaqueteViaje inglaterra = new PaqueteViaje("Viaje a Inglaterra", "Londres");
        encontrados.forEach(inglaterra::agregar);
        inglaterra.agregar(paris);

        Reserva viaje = gestor.crear(Reserva.builder(ana, inglaterra)
                .fechas(ida, ida.plusDays(6))
                .preferencias("Ventanilla; habitación doble")
                .equipaje(2)
                .solicitudEspecial("Menú vegetariano")
                .seguro("Póliza básica")
                .pago(new Pago("Tarjeta", "Ana López"))
                .build());
        System.out.println(viaje.mostrar());

        System.out.println("\n== Cancelación del paquete completo ==");
        gestor.cancelar(viaje.getId());
        System.out.println(viaje.mostrar());
    }
}
