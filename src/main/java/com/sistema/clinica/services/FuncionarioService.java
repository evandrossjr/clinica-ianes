package com.sistema.clinica.services;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.dtos.FuncionarioDTO;
import com.sistema.clinica.models.dtos.mappers.FuncionarioMapper;
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




    public List<FuncionarioDTO> findAll() {
        return funcionarioRepository.findAll().stream().map(FuncionarioMapper::toDTO).toList();
    }

    public FuncionarioDTO findById(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Funcionário não encontrado"));
        return FuncionarioMapper.toDTO(funcionario);
    }

    public FuncionarioDTO insert(FuncionarioDTO dto) {
        if (funcionarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (funcionarioRepository.existsByMatricula(dto.matricula())) {
            throw new IllegalArgumentException("Matrícula já cadastrada.");
        }
        if (funcionarioRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Funcionário já cadastrado.");
        }
        if (funcionarioRepository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Funcionario obj = FuncionarioMapper.toEntity(dto);

        String senhaCriptografada = passwordEncoder.encode(dto.password());
        obj.setPassword(senhaCriptografada);

        if (obj.getUsername() == null || obj.getUsername().isEmpty()) {
            obj.setUsername(obj.getEmail());
        }

        Set<Role> roles = EnumSet.of(Role.ROLE_FUNCIONARIO);
        obj.setRoles(roles);

        Funcionario funcioanrioSalvo =  funcionarioRepository.save(obj);
        return FuncionarioMapper.toDTO(funcioanrioSalvo);
    }

    public void delete(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public FuncionarioDTO update(Long id, FuncionarioDTO dto) {
        try {
            Funcionario entity = funcionarioRepository.findById(id).
                    orElseThrow(() -> new ResourceAccessException("Funcionário não encontrado"));

            Funcionario updates =FuncionarioMapper.toEntity(dto);
            updateData(entity, updates);

            Funcionario updated = funcionarioRepository.save(entity);
            return FuncionarioMapper.toDTO(updated);
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


