package com.sistema.clinica.services;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.enums.Role;
import com.sistema.clinica.repositories.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

@Service
public class FuncionarioService {


    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Funcionario findById(Long id) {
        Optional<Funcionario> obj = funcionarioRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceAccessException("Funcionário não encontrado"));
    }

    public Funcionario insert(Funcionario obj) {
        String senhaCriptografada = passwordEncoder.encode(obj.getPassword());
        obj.setPassword(senhaCriptografada);

        if (obj.getUsername() == null || obj.getUsername().isEmpty()) {
            obj.setUsername(obj.getEmail());
        }

        Set<Role> roles = EnumSet.of(Role.ROLE_FUNCIONARIO);  // Define 'USER' como a role padrão
        obj.setRoles(roles);
        return funcionarioRepository.save(obj);
    }

    public void delete(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public Funcionario update(Long id, Funcionario obj) {
        try {
            Funcionario entity = funcionarioRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com matrícula: " + id));
            updateData(entity, obj);
            return funcionarioRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o Funcionário: " + e.getMessage(), e);
        }
    }

    private void updateData(Funcionario entity, Funcionario obj) {
        entity.setNome(obj.getNome());
        entity.setCpf(obj.getCpf());
        entity.setEmail(obj.getEmail());
        entity.setTelefone(obj.getTelefone());
        entity.setSetor(obj.getSetor());
        entity.setMatricula(obj.getMatricula());
        if (obj.getPassword() != null && !obj.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(obj.getPassword()));

        }


    }

}


