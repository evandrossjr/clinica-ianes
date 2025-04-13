package com.sistema.clinica.security;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.Pessoa;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class PessoaDetails implements UserDetails {

    private final Pessoa pessoa;

    public PessoaDetails(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role;

        if (pessoa instanceof Medico) {
            role = "ROLE_MEDICO";
        } else if (pessoa instanceof Funcionario) {
            role = "ROLE_FUNCIONARIO";
        } else if (pessoa instanceof Paciente) {
            role = "ROLE_PACIENTE";
        } else {
            role = "ROLE_PACIENTE"; // Caso seja uma classe desconhecida ou nula
        }

        return List.of(new SimpleGrantedAuthority(role));
    }


    @Override
    public String getPassword() {
        return pessoa.getPassword();
    }

    @Override
    public String getUsername() {
        return pessoa.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

