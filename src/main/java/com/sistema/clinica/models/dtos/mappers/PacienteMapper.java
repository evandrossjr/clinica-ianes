package com.sistema.clinica.models.dtos.mappers;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.dtos.PacienteDTO;

public class PacienteMapper {
    // Entidade → DTO
    public static PacienteDTO toDTO(Paciente paciente) {
        return new PacienteDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getUsername(),
                null, // nunca retornar a senha
                paciente.getCpf(),
                paciente.getEmail(),
                paciente.getTelefone()
        );
    }

    // DTO → Entidade
    public static Paciente toEntity(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setId(dto.id());
        paciente.setNome(dto.nome());
        paciente.setUsername(dto.username());
        paciente.setCpf(dto.cpf());
        paciente.setEmail(dto.email());
        paciente.setTelefone(dto.telefone());
        return paciente;
    }
}
