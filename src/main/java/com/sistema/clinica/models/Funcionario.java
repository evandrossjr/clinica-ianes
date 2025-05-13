package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
public class Funcionario extends Pessoa{


    @NotBlank
    private String setor;
    @NotBlank
    private int matricula;


    public Funcionario(){}

    public Funcionario(String nome, String username, String password, String cpf, String email, String telefone, Set<Role> roles,  String setor, int matricula) {
        super( nome, username, password, cpf, email, telefone, roles);
        this.setor = setor;
        this.matricula = matricula;
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
