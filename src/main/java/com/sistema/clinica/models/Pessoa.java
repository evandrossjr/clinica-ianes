package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String cpf;
    @NotBlank
    private String email;

    @Pattern(regexp = "\\(?\\d{2}\\)?\\s?\\d{8,9}", message = "Telefone inválido")
    private String telefone;


    @CollectionTable(name = "pessoa_roles", joinColumns = @JoinColumn(name = "pessoa_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();


    public Pessoa() {
    }

    public Pessoa( String nome, String username, String password, String cpf, String email, String telefone, Set<Role> roles) {

        this.nome = nome;
        this.username = username;
        this.password = password;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.roles = roles;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
