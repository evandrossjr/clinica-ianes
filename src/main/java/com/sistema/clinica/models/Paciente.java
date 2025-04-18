package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.Role;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Paciente extends Pessoa {

    public Paciente(){}

    public Paciente( String nome, String username, String password, String cpf, String email, String telefone, Set<Role> roles) {
        super( nome, username, password, cpf, email, telefone, roles);
    }
}
