package com.sistema.clinica.models.dtos.mappers;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.dtos.FuncionarioDTO;

public class FuncionarioMapper {

    public static FuncionarioDTO toDTO(Funcionario funcionario) {
        return new FuncionarioDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getCpf(),
                funcionario.getEmail(),
                funcionario.getTelefone(),
                funcionario.getSetor(),
                funcionario.getMatricula(),
                funcionario.getUsername()
        );
    }

    public static Funcionario formToEntity(FuncionarioFormDTO formDTO) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(formDTO.getNome());
        funcionario.setCpf(formDTO.getCpf());
        funcionario.setEmail(formDTO.getEmail());
        funcionario.setTelefone(formDTO.getTelefone());
        funcionario.setSetor(formDTO.getSetor());
        funcionario.setMatricula(formDTO.getMatricula());
        funcionario.setUsername(formDTO.getEmail());
        return funcionario;
    }

    public static FuncionarioFormDTO toFormDTO(Funcionario funcionario) {
        FuncionarioFormDTO formDTO = new FuncionarioFormDTO();
        formDTO.setNome(funcionario.getNome());
        formDTO.setCpf(funcionario.getCpf());
        formDTO.setEmail(funcionario.getEmail());
        formDTO.setTelefone(funcionario.getTelefone());
        formDTO.setSetor(funcionario.getSetor());
        formDTO.setMatricula(funcionario.getMatricula());
        return formDTO;
    }
}
