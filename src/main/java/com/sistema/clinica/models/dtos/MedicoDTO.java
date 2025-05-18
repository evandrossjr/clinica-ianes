package com.sistema.clinica.models.dtos;
import com.sistema.clinica.models.enums.DiaDaSemana;
import java.util.Set;

public record MedicoDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        int crm,
        String especialidade,
        Set<DiaDaSemana> diasDisponiveis,
        String username
) {

}

