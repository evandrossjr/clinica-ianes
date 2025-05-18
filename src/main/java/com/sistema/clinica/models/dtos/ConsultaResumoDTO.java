package com.sistema.clinica.models.dtos;

public record ConsultaResumoDTO(
        String dataFormatada,
        String horaFormatada,
        String pacienteNome,
        String medicoNome,
        String especialidade,
        String modalidade
) {}