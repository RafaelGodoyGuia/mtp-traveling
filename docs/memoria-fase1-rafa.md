# Traveling — Memoria de la Fase 1 (parte de Rafa)

Arquitectura núcleo: modelo de dominio, contratos comunes, Builder, Composite, pruebas y respuestas del Anexo A, punto 4.

## 1. Modelo de dominio

| Paquete | Clases | Responsabilidad |
| :-- | :-- | :-- |
| `dominio` | `Cliente`, `Pago` | Datos del cliente y del pago (records inmutables). |
| `servicios` | `ServicioViaje`, `ServicioSimple`, `Vuelo`, `Hotel`, `CocheAlquiler`, `PaqueteViaje`, `Proveedor` | Lo que se puede reservar y de dónde sale. |
| `reservas` | `Reserva` (con `Reserva.Builder`), `GestorReservas` | La reserva y los casos de uso del cliente. |

```
             «interface» ServicioViaje
   getNombre() getDestino() calcularPrecio() mostrar() cancelar() isCancelado()
                 ▲                              ▲
       ServicioSimple (abstracta)          PaqueteViaje ◇──── 0..* ServicioViaje
        ▲        ▲          ▲
      Vuelo    Hotel   CocheAlquiler

  Reserva ──1──> ServicioViaje        GestorReservas ──*──> «interface» Proveedor
     │  ──1──> Cliente                       └──*──> Reserva
     └ 0..1 ─> Pago
```

### Contratos comunes

- **`ServicioViaje`** es la única interfaz que conoce el resto del sistema. Vuelo, hotel, coche y paquete la cumplen igual, así que la reserva, el gestor y los futuros patrones (Decorator, Strategy, State, Observer, Command) trabajan contra ella y no contra clases concretas.
- **`ServicioSimple`** concentra lo que comparten las hojas (destino, estado de cancelación, formato de `mostrar`). Cada servicio concreto solo aporta sus datos propios y su forma de calcular el precio (vuelo: precio fijo; hotel: noches × precio por noche; coche: días × precio por día).
- **`Proveedor`** (`buscar(destino)`) es la interfaz que espera la plataforma de cualquier fuente de servicios. Los proveedores externos con API propia (`findRooms()`, `reserveRoom()`…) se conectarán con un Adapter que la implemente, sin que `GestorReservas` cambie.
- **`GestorReservas`** cubre los casos de uso del enunciado: buscar servicios disponibles, crear, consultar y cancelar una reserva.

Los importes son `BigDecimal` para no perder céntimos por redondeo binario.

## 2. Patrón creacional aplicado: Builder

**Problema.** Una reserva puede llevar cliente, fechas, destino, preferencias de asiento o habitación, equipaje, solicitudes especiales, seguro e información de pago, y casi nada es obligatorio. Con constructores habría que escribir uno por combinación (constructor telescópico) o pasar listas largas de `null`, ilegibles y fáciles de equivocar porque varios parámetros son del mismo tipo.

**Solución.** `Reserva` tiene constructor privado y se crea solo con `Reserva.builder(cliente, servicio)`:

```java
Reserva r = Reserva.builder(ana, paqueteInglaterra)
        .fechas(ida, vuelta)
        .preferencias("Ventanilla; habitación doble")
        .equipaje(2)
        .solicitudEspecial("Menú vegetariano")
        .seguro("Póliza básica")
        .pago(new Pago("Tarjeta", "Ana López"))
        .build();
```

**Decisiones.**
- Lo obligatorio (cliente y servicio) va en `builder(...)`: no se puede construir una reserva sin ellos.
- Cada paso valida su dato (nulos, equipaje negativo) y `build()` valida la combinación (la fecha de fin no puede ser anterior a la de inicio).
- La reserva resultante es inmutable salvo la cancelación; los campos opcionales se exponen como `Optional`.
- Un único Builder sirve para los tres tipos de servicio porque lo específico de cada tipo vive en el propio servicio (`Vuelo`, `Hotel`, `CocheAlquiler`), no en la reserva. Un tipo de servicio nuevo no obliga a tocar el Builder.
- Es el Builder "fluido" de Bloch (clase estática anidada) y no la variante GoF con `Director`: no hay varias representaciones del mismo proceso de construcción que justifiquen un director.

## 3. Patrón estructural aplicado: Composite

**Problema.** Un paquete de viaje contiene vuelos, hoteles, coches y otros paquetes, con anidamiento arbitrario. Calcular el precio, mostrar el contenido y cancelar debe funcionar igual con un servicio suelto que con un paquete entero.

**Solución.** `ServicioViaje` es el *componente*; `Vuelo`, `Hotel` y `CocheAlquiler` son las *hojas*; `PaqueteViaje` es el *compuesto*, que guarda una lista de `ServicioViaje` y delega en ella:

| Operación | Hoja | Paquete |
| :-- | :-- | :-- |
| `calcularPrecio()` | Su precio | Suma de los precios de sus hijos |
| `mostrar(sangria)` | Una línea | Su línea y la de cada hijo con más sangría |
| `cancelar()` | Se marca cancelada | Cancela a todos sus hijos (en cascada) |
| `isCancelado()` | Su marca | Cancelado si lo están todos sus hijos (vacío: no) |

El ejemplo del enunciado (Viaje a Inglaterra con Extensión a París) se reserva y se cancela con las mismas llamadas que un vuelo:

```
+ Paquete Viaje a Inglaterra: 960.00 €
    - Vuelo IB3166 Madrid → Londres (2027-03-01): 120.00 €
    - Hotel Strand Palace en Londres, 4 noches: 380.00 €
    - Coche Mini Cooper en Londres, 4 días: 160.00 €
    + Paquete Extensión a París: 300.00 €
        - Vuelo AF1381 Londres → París (2027-03-05): 80.00 €
        - Hotel Le Marais en París, 2 noches: 220.00 €
```

**Decisiones.**
- Composite "seguro": `agregar`/`quitar` están solo en `PaqueteViaje`, no en la interfaz. Así una hoja nunca ofrece operaciones que no tienen sentido para ella; a cambio, quien monta paquetes tiene que saber que trabaja con un `PaqueteViaje`, algo que sí sabe siempre.
- `agregar` rechaza ciclos (un paquete dentro de sí mismo, directa o indirectamente), que harían infinitos `calcularPrecio` y `mostrar`.
- `getHijos()` devuelve una copia inmodificable: el árbol solo cambia por `agregar` y `quitar`.
- El estado del paquete se deriva de sus hijos en vez de guardarse aparte, para que nunca se contradigan.

## 4. Pruebas

`mvn package` ejecuta 16 pruebas JUnit 5 y `Main` es una demostración por consola de todos los casos de uso.

| Clase de prueba | Qué comprueba |
| :-- | :-- |
| `GestorReservasTest` | Un caso de uso completo (buscar → crear → consultar → cancelar) para **vuelo, hotel y coche**; reserva de un paquete; reserva inexistente. |
| `ReservaBuilderTest` | Reserva mínima y completa, destino por defecto, fechas invertidas, obligatorios nulos y equipaje negativo. |
| `PaqueteViajeTest` | Precio total en varios niveles (960 €), sangría de `mostrar`, cancelación en cascada, cancelación parcial, uso uniforme hoja/paquete, rechazo de ciclos y paquete vacío. |

## 5. Informe final de la fase de patrones (Anexo A, punto 4)

### ¿Qué clases permanecieron sin cambios a lo largo de la evolución?

`Cliente`, `Pago`, `Vuelo`, `Hotel`, `CocheAlquiler`, `ServicioSimple` y `PaqueteViaje`. La interfaz `ServicioViaje` se fijó en la fase de creación pensando ya en el paquete, de modo que la fase estructural no la tocó: el Decorator de servicios adicionales (seguro, protección de cancelación, equipaje adicional, prioridad) envuelve un `ServicioViaje` e implementa `ServicioViaje`, y el Adapter de cada proveedor implementa `Proveedor`. Ninguno de los dos necesita cambiar las hojas ni el paquete. El Builder de `Reserva` tampoco cambió al añadir paquetes: una reserva de un paquete es una reserva de un `ServicioViaje` más.

### ¿Qué clases tuvieron que modificarse?

- **`Reserva`**: en la fase de comportamiento, su cancelación pasa a depender del estado (State). La versión inicial delegaba `cancelar()` directamente en el servicio; con State, `cancelar()` y `confirmar()` delegan en el objeto estado, que decide si la transición es válida (por ejemplo, una reserva completada no puede cancelarse). También pasa a avisar a sus observadores al cambiar de estado (Observer).
- **`Reserva.calcularPrecio()`**: deja de devolver el precio del servicio tal cual y lo pasa por la política de precios activa (Strategy).
- **`GestorReservas`**: sus operaciones se encapsulan en comandos (Command) para que web, móvil y administración las invoquen, encolen, registren o ejecuten más tarde sin conocer al gestor.

Los cambios se concentran en `Reserva` y `GestorReservas`, que son el punto de entrada de los casos de uso; el árbol de servicios no se toca.

### ¿Dónde se introdujeron interfaces para reducir el acoplamiento?

- **`ServicioViaje`**: separa a todos los clientes (reserva, gestor, decoradores, políticas de precio) de los tipos concretos de servicio y de si es una hoja o un paquete.
- **`Proveedor`**: separa el gestor de la API concreta de cada proveedor externo. Al ser funcional, en las pruebas un proveedor es una lambda.
- En las fases siguientes, con el mismo criterio: la interfaz de la política de precios (Strategy), la del estado de la reserva (State), la del observador de reservas (Observer) y la del comando (Command). Cada una permite añadir una implementación nueva sin modificar las existentes.

### ¿Qué decisiones de diseño facilitaron las ampliaciones posteriores?

1. **Programar contra `ServicioViaje` desde el primer día.** Composite, Decorator y Adapter encajan como implementaciones nuevas de interfaces que ya existían.
2. **Dejar lo específico de cada servicio en el servicio**, no en la reserva. Un tipo de servicio nuevo (tren, crucero) es una subclase de `ServicioSimple` y no obliga a tocar `Reserva`, su Builder ni `GestorReservas`.
3. **Construir la reserva con Builder.** Añadir un campo opcional nuevo es añadir un método al Builder; ningún código que ya crea reservas deja de compilar.
4. **Reserva inmutable salvo su ciclo de vida.** Al llegar State, el único comportamiento mutable estaba ya aislado en `cancelar()`, que es lo que se sustituyó.
5. **Importes en `BigDecimal` y precio calculado bajo demanda** (no guardado). Strategy y Decorator pueden alterar el precio sin datos desincronizados.

### ¿La introducción de un patrón afectó a otro patrón?

Sí, en varios puntos:

- **Composite ↔ Decorator.** Como ambos implementan `ServicioViaje`, se combinan sin código extra: se puede decorar un paquete entero (seguro para todo el viaje) o un servicio dentro de un paquete (equipaje adicional solo en un vuelo). La contrapartida es que el paquete ve al decorador como un hijo más y `mostrar()` debe enseñar los extras sin romper la sangría del árbol.
- **Composite ↔ Strategy.** La política de precios se aplica sobre `calcularPrecio()` del componente raíz; como el paquete ya suma recursivamente, una sola estrategia sirve para un servicio o para un paquete completo.
- **Composite ↔ State.** Cancelar una reserva de paquete debe cancelar todo el árbol. El estado decide *si* se puede cancelar; el Composite se encarga de *propagarlo*.
- **State ↔ Observer.** Las transiciones de estado son justo los eventos que se notifican (confirmación → aviso al cliente, puntos de fidelización y estadísticas).
- **Command ↔ Builder.** El comando "crear reserva" recibe la reserva ya construida por el Builder, así que su validación ocurre antes de encolar la operación.

### ¿Hay alguna parte del diseño que se replantearía ahora?

- **El seguro como campo de `Reserva`.** El enunciado lo pide como dato de la reserva en la fase 1, pero en la fase 2 el seguro de viaje es un servicio adicional con Decorator. Ahora hay dos formas de expresar lo mismo; habría que quedarse solo con el decorador y dejar el campo para la póliza contratada fuera de Traveling.
- **La cancelación en las hojas.** La marca `cancelado` vive en cada servicio y State controla el ciclo de vida de la reserva: son dos fuentes de verdad. Lo replantearía para que el servicio no guarde estado y sea la reserva (su State) quien sepa si está cancelada.
- **Composite seguro frente a transparente.** La variante segura evita que una hoja ofrezca `agregar`, pero obliga a `instanceof` en la detección de ciclos y en las pruebas que recorren el árbol. Es aceptable con un único tipo compuesto; con más tipos compuestos se valoraría subir `getHijos()` a la interfaz (vacío en las hojas).
- **`GestorReservas` en memoria.** Guarda las reservas en un `Map`. Para la fase 2 convendría extraer una interfaz de repositorio, de modo que Spring Boot pueda sustituir el almacenamiento sin tocar el gestor.
