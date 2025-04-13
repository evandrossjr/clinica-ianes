package com.sistema.clinica.security;

import com.sistema.clinica.models.Pessoa;
import com.sistema.clinica.repositories.PessoaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PessoaDetailsService implements UserDetailsService {

    private final PessoaRepository pessoaRepository;



    public PessoaDetailsService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Tentando logar com: " + username);

        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado");
                    return new UsernameNotFoundException("Usuário não encontrado");
                });

        System.out.println("Usuário encontrado: " + pessoa.getUsername());
        System.out.println("Roles: " + pessoa.getRoles());
        System.out.println("Classe: " + pessoa.getClass().getSimpleName());

        return new PessoaDetails(pessoa);
    }

}

