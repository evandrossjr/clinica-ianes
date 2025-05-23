package com.sistema.clinica.services;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sistema.clinica.models.dtos.PacienteDTO;
import com.sistema.clinica.models.dtos.mappers.FuncionarioMapper;
import com.sistema.clinica.models.dtos.mappers.PacienteMapper;
import com.sistema.clinica.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.repositories.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PacienteService {


    @Autowired
    private PacienteRepository pacienteRepository ;

    private final PasswordEncoder passwordEncoder;

    public PacienteService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



    public List<PacienteDTO> findAll(){

        return pacienteRepository.findAll().stream().map(PacienteMapper::toDTO).toList();
    }

    public PacienteDTO findById(Long id){
        Paciente paciente = pacienteRepository.findById(id).
                orElseThrow(()-> new ResourceAccessException("Paciente não encontrado"));
        return PacienteMapper.toDTO(paciente);
    }

    public PacienteDTO insert(PacienteDTO dto){
        if (pacienteRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (pacienteRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Usuário já cadastrado.");
        }

        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }


        Paciente obj = PacienteMapper.toEntity(dto);

        String senhaCriptografada = passwordEncoder.encode(dto.password());
        obj.setPassword(senhaCriptografada);

        if (obj.getUsername() == null || obj.getUsername().isEmpty()) {
            obj.setUsername(obj.getEmail());
        }

        Set<Role> roles = EnumSet.of(Role.ROLE_PACIENTE);  // Define 'USER' como a role padrão
        obj.setRoles(roles);


        Paciente pacienteSalvo = pacienteRepository.save(obj);
        return PacienteMapper.toDTO(pacienteSalvo);
    }

    public void delete(Long id){
        pacienteRepository.deleteById(id);
    }

    public PacienteDTO update(Long id, PacienteDTO dto){
        try{
            Paciente entity = pacienteRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com matrícula: " + id));

            Paciente updates = PacienteMapper.toEntity(dto);
            updateData(entity, updates);

            Paciente updated = pacienteRepository.save(entity);
            return PacienteMapper.toDTO(updated);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o Paciente: " + e.getMessage(), e);
        }
    }

    private void updateData(Paciente entity, Paciente obj) {
        entity.setNome(obj.getNome());
        entity.setCpf(obj.getCpf());
        entity.setEmail(obj.getEmail());
        entity.setTelefone(obj.getTelefone());
        if (obj.getPassword() != null && !obj.getPassword().isBlank()) {
            if (!obj.getPassword().startsWith("$2a$")) {
                entity.setPassword(passwordEncoder.encode(obj.getPassword()));
            }
        }


    }


}


