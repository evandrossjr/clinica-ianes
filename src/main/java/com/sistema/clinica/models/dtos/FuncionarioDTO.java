package com.sistema.clinica.models.dtos;

public record FuncionarioDTO (Long id,
                              String nome,
                              String username,
                              String password,
                              String cpf,
                              String email,
                              String telefone,
                              String setor,
                              int matricula){
}
