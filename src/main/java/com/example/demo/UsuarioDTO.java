package com.example.demo;

// DTO = Data Transfer Object
// Este é o objeto que esperamos receber do cliente.
// Note que ele não tem o 'id', pois o cliente não deve enviá-lo.
public record UsuarioDTO(
        String nome,
        String email
) {}