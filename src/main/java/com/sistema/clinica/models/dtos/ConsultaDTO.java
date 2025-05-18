package com.sistema.clinica.models.dtos;

import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.enums.Modalidade;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaDTO(Long id,
                          Paciente paciente,
                          Medico medico,
                          LocalDate data,
                          LocalTime hora,
                          boolean pagamentoRealizado,
                          Modalidade modalidade) {
}
