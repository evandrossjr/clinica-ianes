package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Medico extends Pessoa{

    @NotBlank
    private int crm;
    @NotBlank
    private String especialidade;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<DiaDaSemana> diasDisponiveis;

    public Medico(){}


    public Medico(String nome, String username, String password, String cpf, String email, String telefone, Set<Role> roles, int crm, String especialidade, Set<DiaDaSemana> diasDisponiveis) {
        super(nome, username, password, cpf, email, telefone, roles);
        this.crm = crm;
        this.especialidade = especialidade;
        this.diasDisponiveis = diasDisponiveis;
    }

    public Medico(String nome, String username, String password, String cpf, String email, String telefone, Set<Role> roles) {
        super(nome, username, password, cpf, email, telefone, roles);
    }



    public int getCrm() {
        return crm;
    }

    public void setCrm(int crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Set<DiaDaSemana> getDiasDisponiveis() {
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(Set<DiaDaSemana> diasDisponiveis) {
        this.diasDisponiveis = diasDisponiveis;
    }


}


