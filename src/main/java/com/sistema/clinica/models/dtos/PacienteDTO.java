package com.sistema.clinica.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PacienteDTO(Long id,
                          @NotBlank String nome,
                          String username,
                          String password,
                          @NotNull String cpf,
                          @Email String email,
                          @NotNull String telefone) {
}
