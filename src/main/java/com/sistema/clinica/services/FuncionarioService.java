package com.sistema.clinica.services;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.dtos.FuncionarioDTO;
import com.sistema.clinica.models.dtos.mappers.FuncionarioFormDTO;
import com.sistema.clinica.models.dtos.mappers.FuncionarioMapper;
import com.sistema.clinica.models.enums.Role;
import com.sistema.clinica.repositories.FuncionarioRepository;
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

    public FuncionarioFormDTO buscarParaEdicao(Long id) {
        return funcionarioRepository.findById(id)
                .map(FuncionarioMapper::toFormDTO)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    public FuncionarioDTO insert(FuncionarioFormDTO formDTO) {
        Funcionario funcionario = FuncionarioMapper.formToEntity(formDTO);
        funcionario.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        funcionario.setRoles(Set.of(Role.ROLE_FUNCIONARIO));

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        return FuncionarioMapper.toDTO(funcionarioSalvo);
    }

    public void delete(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public FuncionarioDTO update(Long id, FuncionarioFormDTO formDTO) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        funcionario.setNome(formDTO.getNome());
        funcionario.setCpf(formDTO.getCpf());
        funcionario.setEmail(formDTO.getEmail());
        funcionario.setTelefone(formDTO.getTelefone());
        funcionario.setSetor(formDTO.getSetor());
        funcionario.setMatricula(formDTO.getMatricula());

        // Atualiza senha apenas se for informada
        if (formDTO.getPassword() != null && !formDTO.getPassword().isEmpty()) {
            funcionario.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        }

        return FuncionarioMapper.toDTO(funcionarioRepository.save(funcionario));
    }





}


