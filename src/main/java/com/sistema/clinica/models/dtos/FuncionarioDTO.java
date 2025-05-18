package com.sistema.clinica.models.dtos;

public record FuncionarioDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        String setor,
        int matricula,
        String username
) {}