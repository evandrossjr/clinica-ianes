package com.sistema.clinica.models.dtos;

public class MedicoDTO {
    private Long id;
    private String nome;

    public MedicoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}

