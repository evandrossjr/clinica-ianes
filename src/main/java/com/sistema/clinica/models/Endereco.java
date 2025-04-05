package com.sistema.clinica.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.id.IntegralDataTypeHolder;

@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String CEP;
    private String rua;
    private Integer numero;
    private String bairro;
    private String estado;
    private String cidade;

    public Endereco() {
    }
}
