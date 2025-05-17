package com.sistema.clinica.models.dtos;

public record PacienteDTO(Long id,
                          String nome,
                          String username,
                          String password,
                          String cpf,
                          String email,
                          String telefone) {
}
