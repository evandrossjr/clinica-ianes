package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.DiaDaSemana;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Medico{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private Integer telefone;
    private int crm;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<DiaDaSemana> diasDisponiveis;

	
    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;


    public Medico(Long id, String nome, String cpf, String email, Integer telefone, int crm, Set<DiaDaSemana> diasDisponiveis, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.crm = crm;
        this.diasDisponiveis = diasDisponiveis;
        this.endereco = endereco;
    }

    public Medico() {
        
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getTelefone() {
        return telefone;
    }
    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }
    public Endereco getEndereco() {
        return endereco;
    }
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    
    public int getCrm() {
        return crm;
    }
    public void setCrm(int crm) {
        this.crm = crm;
    }

    public Set<DiaDaSemana> getDiasDisponiveis() {
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(Set<DiaDaSemana> diasDisponiveis) {
        this.diasDisponiveis = diasDisponiveis;
    }
}


