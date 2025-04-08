package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.Modalidade;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Cliente() {
    }


    public Cliente(String nome, String cpf, String email, Integer telefone, Long id) {
        super(nome, cpf, email, telefone);
        this.id = id;
    }
}
