package com.sistema.clinica.models.dtos;

public record MedicoResumoDTO(
        Long id,
        String nome,
        String especialidade,
        int crm
) {}