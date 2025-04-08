package com.sistema.clinica.models;

import jakarta.persistence.*;

@Entity
public class Paciente extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Paciente() {
    }


    public Paciente(String nome, String cpf, String email, Integer telefone, Long id) {
        super(nome, cpf, email, telefone);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
