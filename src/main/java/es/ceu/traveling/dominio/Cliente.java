package es.ceu.traveling.dominio;

import java.util.Objects;

public record Cliente(String nombre, String email) {
    public Cliente {
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(email, "email");
    }
}
