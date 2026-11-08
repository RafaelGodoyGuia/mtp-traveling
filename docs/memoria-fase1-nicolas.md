# Memoria de Nicolás — Patrones de comportamiento

## 1. Alcance

Esta parte de la memoria describe los patrones de comportamiento asignados en la Fase 1 de Traveling: State, Observer y Command. Se integran con el modelo de reservas existente, manteniendo el Builder de `Reserva` y el contrato `ServicioViaje` utilizado por los servicios simples y los paquetes Composite.

## 2. State: ciclo de vida de una reserva

### Problema

Una reserva no puede aceptar cualquier operación en cualquier momento. Por ejemplo, una reserva completada no se puede cancelar y una cancelada no se puede confirmar. Implementar estas reglas mediante condicionales repartidos por `Reserva` haría que la clase creciera y sería difícil incorporar estados nuevos.

### Solución aplicada

`Reserva` mantiene una referencia a `EstadoReserva`. Cada estado concreto implementa las operaciones posibles:

- `EstadoCreada`: permite pasar a pendiente de pago, confirmar directamente o cancelar.
- `EstadoPendientePago`: permite confirmar o cancelar.
- `EstadoConfirmada`: permite completar o cancelar.
- `EstadoCompletada`: no permite más transiciones.
- `EstadoCancelada`: no permite reactivar ni confirmar la reserva.

Las operaciones públicas (`pasarAPendientePago`, `confirmar`, `completar` y `cancelar`) delegan en el estado actual. Una transición no permitida lanza `IllegalStateException`. La cancelación del servicio asociado se realiza después de validar la transición, por lo que el patrón State también protege el servicio simple o el paquete Composite.

### Clases principales

`EstadoReserva` define el contrato; las cinco clases `Estado...` contienen las reglas de cada estado; `Reserva` conserva el estado actual y expone las operaciones al resto del sistema.

## 3. Observer: reacción a los cambios

### Problema

Al cambiar una reserva, varios subsistemas deben reaccionar: notificar al cliente, actualizar puntos y registrar estadísticas. La reserva no debe depender de las clases concretas de esos subsistemas.

### Solución aplicada

`ObservadorReserva` define el método `actualizar(EventoReserva)`. La reserva mantiene una lista de observadores, permite suscribirlos y eliminarlos y, cuando cambia su estado, publica un `EventoReserva` con la reserva, el tipo de evento, el estado anterior, el nuevo y la fecha.

Se incluyen tres observadores:

- `NotificacionCliente`: conserva mensajes asociados a los cambios de estado.
- `SistemaFidelizacion`: suma puntos al producirse una confirmación.
- `SistemaEstadisticas`: contabiliza los eventos por tipo.

Los observadores pueden añadirse sin modificar `Reserva`, lo que reduce el acoplamiento y facilita futuras ampliaciones. La reserva solo conoce la interfaz `ObservadorReserva`.

## 4. Command: encapsulación de operaciones

### Problema

Las operaciones de crear, confirmar y cancelar no deben depender de la consola ni de la futura interfaz. Además, el enunciado pide que puedan almacenarse, encolarse y ejecutarse posteriormente.

### Solución aplicada

`Comando` define `ejecutar()`. `CrearReservaCommand`, `ConfirmarReservaCommand`, `CancelarReservaCommand`, `PasarAPendientePagoCommand` y `CompletarReservaCommand` encapsulan cada petición como un objeto.

`EjecutorComandos` actúa como invocador: permite ejecutar inmediatamente, añadir comandos a una cola, ejecutar el siguiente o procesar toda la cola y consultar un historial de comandos ejecutados. Los comandos no implementan las reglas del ciclo de vida: delegan en `Reserva`, por lo que State sigue siendo el responsable de validar las transiciones.

## 5. Colaboración entre patrones

El flujo de confirmación ilustra la colaboración:

1. El cliente de la aplicación solicita una confirmación creando un `ConfirmarReservaCommand`.
2. `EjecutorComandos` ejecuta el comando.
3. El comando invoca `Reserva.confirmar()`.
4. `Reserva` delega en el objeto `EstadoReserva` actual.
5. Si la transición está permitida, la reserva cambia a `EstadoConfirmada`.
6. La reserva publica un evento Observer.
7. Los observadores suscritos reaccionan de forma independiente.

Command separa la petición de su origen; State valida la operación; Observer distribuye el resultado. Ninguno sustituye la responsabilidad de los otros.

## 6. Patrones de comportamiento descartados

### Chain of Responsibility

Este patrón pasa una petición por una cadena de posibles gestores hasta que uno la atiende. En Traveling, una operación sobre una reserva tiene un destino conocido: la propia reserva y su estado actual. No existe una cadena de validadores o gestores alternativos que justifique introducir esa estructura.

### Interpreter

Está orientado a representar e interpretar una gramática o lenguaje de expresiones. Traveling no recibe consultas expresadas en un lenguaje propio ni necesita construir árboles sintácticos; sus operaciones son métodos Java definidos.

### Iterator

Proporciona una forma uniforme de recorrer colecciones sin exponer su representación. Las colecciones del proyecto pueden recorrerse con las herramientas estándar de Java y no existe un recorrido especializado de reservas que requiera un iterador propio. Por ello no aporta una mejora suficiente para justificar su implementación.

### Memento

Permite capturar y restaurar el estado interno de un objeto. Los requisitos de esta fase no piden deshacer operaciones ni recuperar versiones históricas de una reserva. El historial de comandos registra operaciones ejecutadas, pero no implementa deshacer; Memento sería necesario si se añadiera esa funcionalidad.

### Template Method

Define el esqueleto de un algoritmo en una superclase y deja algunos pasos a las subclases. Las operaciones del ciclo de vida no son variantes de un mismo algoritmo con pasos fijos: sus reglas dependen del estado actual. State representa directamente ese requisito y evita forzar una jerarquía de algoritmos.

### Visitor

Permite añadir operaciones a una estructura de objetos mediante visitantes. Aunque Traveling contiene paquetes Composite, las operaciones requeridas (precio, mostrar y cancelar) ya forman parte del contrato uniforme `ServicioViaje`. No se necesitan operaciones externas complejas sobre una jerarquía estable que justifiquen Visitor.

