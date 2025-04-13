package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Set;

@Entity
public class Funcionario extends Pessoa{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String setor;
    private int matricula;


    public Funcionario(){}

    public Funcionario(Long id, String nome, String username, String password, String cpf, String email, String telefone, Set<Role> roles, Long id1, String setor, int matricula) {
        super(id, nome, username, password, cpf, email, telefone, roles);
        this.id = id1;
        this.setor = setor;
        this.matricula = matricula;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }


}
