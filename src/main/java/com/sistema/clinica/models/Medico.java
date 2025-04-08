package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.DiaDaSemana;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Medico extends Pessoa{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int crm;
    private String especialidade;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<DiaDaSemana> diasDisponiveis;

    public Medico() { }

    public Medico(String nome, String cpf, String email, Integer telefone, Long id, int crm, String especialidade, Set<DiaDaSemana> diasDisponiveis) {
        super(nome, cpf, email, telefone);
        this.id = id;
        this.crm = crm;
        this.especialidade = especialidade;
        this.diasDisponiveis = diasDisponiveis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


