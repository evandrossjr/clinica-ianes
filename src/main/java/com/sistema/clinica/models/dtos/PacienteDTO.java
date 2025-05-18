package com.sistema.clinica.models.dtos;

import jakarta.validation.constraints.NotBlank;

public record PacienteDTO(Long id,
                          @NotBlank String nome,
                          @NotBlank String username,
                          @NotBlank String password,
                          @NotBlank String cpf,
                          @NotBlank String email,
                          String telefone) {
}
