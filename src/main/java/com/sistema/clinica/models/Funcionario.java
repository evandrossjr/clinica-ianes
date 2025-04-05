package com.sistema.clinica.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Funcionario {

    @Id
    private Long id;

    public Funcionario() {
    }
}
