package com.sistema.clinica.models.dtos;

import com.sistema.clinica.models.enums.DiaDaSemana;
import jakarta.validation.constraints.*;

import java.util.Set;

public class MedicoFormDTO {
    @NotBlank private String nome;
    @NotBlank private String cpf;
    @NotBlank @Email private String email;
    @NotBlank private String telefone;
    @NotNull private Integer crm;
    @NotBlank private String especialidade;
    @NotNull private Set<DiaDaSemana> diasDisponiveis;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Integer getCrm() { return crm; }
    public void setCrm(Integer crm) { this.crm = crm; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public Set<DiaDaSemana> getDiasDisponiveis() { return diasDisponiveis; }
    public void setDiasDisponiveis(Set<DiaDaSemana> diasDisponiveis) { this.diasDisponiveis = diasDisponiveis; }
}