# Memoria Técnica - Fase 1: Justificación y Descarte de Patrones

Autor: Jose (Responsable de Integraciones Externas, Modificadores y Tarifas)

Esta sección de la memoria detalla por qué hemos elegido ciertos patrones para resolver los problemas de la plataforma Traveling y por qué hemos dejado fuera otros. La idea principal ha sido mantener el código limpio, fácil de probar y preparado para conectar con React y Spring Boot en la siguiente fase, sin complicarnos la vida con patrones que no necesitamos.

## 1. Patrones Estructurales y de Comportamiento Utilizados

### Adapter (Estructural)

Por qué lo usamos: En la vida real, las APIs de los hoteles, aerolíneas o empresas de alquiler no están hechas a nuestra medida. Por ejemplo, el sistema de un hotel viejo usa un método findRooms(), pero nuestro sistema espera que todo sea un ServicioViaje con el método getPrecio(). En lugar de ensuciar nuestro código intentando que encajen a la fuerza, creamos un Adapter. Funciona como un enchufe adaptador cuando viajas a otro país: envuelve la clase rara del proveedor externo y la hace compatible con nuestro sistema estándar.

### Decorator (Estructural)

Por qué lo usamos: Los usuarios siempre quieren añadir extras a sus viajes (seguro médico, maleta adicional, embarque prioritario). Si usáramos herencia tradicional, tendríamos que crear clases como VueloConSeguro, VueloConSeguroYMaleta, VueloSoloConMaleta... terminaríamos con cientos de clases. El Decorator nos permite coger un servicio base (como un Vuelo) y ponerle "capas" por encima en tiempo de ejecución. Así, añadimos funcionalidades de forma dinámica sin tocar el código original ni crear una explosión de subclases.

### Strategy (Comportamiento)

Por qué lo usamos: El precio de un viaje cambia dependiendo de quién lo compre (un cliente normal, una empresa con convenio, o alguien usando puntos de fidelidad). Sin este patrón, tendríamos un archivo gigante lleno de if / else para calcular los descuentos. Con Strategy, sacamos cada fórmula matemática a su propia clase. Si mañana el departamento de marketing inventa una tarifa nueva, solo creamos un archivo nuevo sin tener que modificar ni romper el código que ya funciona.

## 2. Catálogo Razonado de Patrones Descartados

### Patrones Creacionales Descartados

- **Singleton:** Sirve para asegurar que solo exista una instancia de una clase en todo el programa. Lo descartamos porque suele dar muchos problemas a la hora de hacer las pruebas unitarias (JUnit). Además, en la Fase 2 vamos a usar Spring Boot, que ya gestiona las inyecciones de dependencias como Singletons por defecto, así que hacerlo a mano ahora era reinventar la rueda.

- **Factory Method y Abstract Factory:** Estos patrones son geniales cuando necesitas crear familias enteras de objetos relacionados de golpe. Sin embargo, para crear nuestras reservas usamos el patrón Builder (implementado por Rafa). Una reserva de viaje tiene demasiados atributos opcionales (fechas, tipo de coche, extras, número de personas), y los Factory nos obligarían a pasar un montón de parámetros nulos en los constructores. El Builder es mucho más cómodo para construir cosas paso a paso.

- **Prototype:** Se usa para clonar objetos que son muy costosos de crear desde cero. En Traveling, cada reserva es bastante única (distintos clientes, fechas concretas, asientos). No nos aporta nada copiar la reserva de otra persona, es más rápido y lógico instanciar una nueva.

### Patrones Estructurales Descartados

- **Facade:** Sirve para crear una interfaz súper sencilla que esconda un sistema gigantesco y muy complejo por detrás. Nuestro sistema de reservas todavía está muy bien separado en paquetes y es fácil de entender. No tenemos un código tan desordenado o masivo como para justificar ponerle una "fachada" delante.

- **Bridge:** Se usa cuando tienes dos jerarquías de clases distintas que pueden cambiar de forma independiente (por ejemplo, formas geométricas por un lado y colores por otro). En nuestro modelo de dominio no tenemos esta necesidad de separar la abstracción de la implementación a ese nivel.

- **Flyweight:** Este patrón es para ahorrar memoria RAM cuando tienes que crear miles o millones de objetos idénticos (como los árboles en un videojuego). Como nuestras reservas y servicios no comparten un estado idéntico masivo, usar Flyweight solo complicaría el código sin darnos ninguna mejora real de rendimiento.

- **Proxy:** Se usa para poner un intermediario que controle el acceso a un objeto (por seguridad, o para cargar cosas pesadas de la base de datos solo cuando se necesitan). De momento, en nuestra lógica de negocio pura en Java no necesitamos restricciones de acceso ni "lazy loading" a ese nivel, así que el acceso directo a las clases es perfectamente seguro y eficiente.
