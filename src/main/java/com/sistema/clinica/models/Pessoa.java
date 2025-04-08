package com.sistema.clinica.models;

import jakarta.persistence.*;


@MappedSuperclass
public abstract class Pessoa {


    private String nome;
    private String cpf;
    private String email;
    private Integer telefone;


    public Pessoa() {
    }

    public Pessoa(String nome, String cpf, String email, Integer telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
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
}
