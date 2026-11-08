package es.ceu.traveling.reservas.comandos;

/** Command: encapsula una operación que puede ejecutarse sin conocer quién la solicita. */
@FunctionalInterface
public interface Comando {
    void ejecutar();
}
