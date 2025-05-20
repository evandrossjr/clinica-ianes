package com.sistema.clinica.models.dtos.mappers;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.dtos.FuncionarioDTO;

public class FuncionarioMapper {
    // Entidade → DTO
    public static FuncionarioDTO toDTO(Funcionario funcionario) {
        return new FuncionarioDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getUsername(),
                null, // nunca retornar a senha
                funcionario.getCpf(),
                funcionario.getEmail(),
                funcionario.getTelefone(),
                funcionario.getSetor(),
                funcionario.getMatricula()
                
        );
    }

    // DTO → Entidade
    public static Funcionario toEntity(FuncionarioDTO dto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(dto.id());
        funcionario.setNome(dto.nome());
        funcionario.setUsername(dto.username());
        funcionario.setCpf(dto.cpf());
        funcionario.setEmail(dto.email());
        funcionario.setTelefone(dto.telefone());
        funcionario.setSetor(dto.setor());
        funcionario.setMatricula(dto.matricula());
        return funcionario;
    }
}
