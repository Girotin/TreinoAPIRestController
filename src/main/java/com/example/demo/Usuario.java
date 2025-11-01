package com.example.demo;

// Este é o nosso "Recurso" principal.
// Ele tem um ID, que nós vamos gerar.
public record Usuario(
        Long id,
        String nome,
        String email
) {}