package com.sistema.clinica.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PacienteDTO(Long id,
                          @NotBlank String nome,
                          String username,
                          String password,
                          String cpf,
                          String email,
                          String telefone) {
}
