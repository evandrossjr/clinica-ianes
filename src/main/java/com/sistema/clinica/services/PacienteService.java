package com.sistema.clinica.services;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.sistema.clinica.models.dtos.PacienteDTO;
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
        //o stream decompõe a list, a função map transforma paciente em pacienteDTO e o collector coloca tudo na lista novamente
        return pacienteRepository.findAll().
                stream().
                map(PacienteMapper::toDTO).
                collect(Collectors.toList());
    }

    public PacienteDTO findById(Long id){
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(()-> new ResourceAccessException("Paciente não encontrado"));
        return PacienteMapper.toDTO(paciente);
    }

    public PacienteDTO insert(PacienteDTO dto){
        Paciente paciente = PacienteMapper.toEntity(dto);

        if(dto.password() != null && !dto.password().isEmpty()){
            paciente.setPassword(passwordEncoder.encode(dto.password()));
        }else {
            throw new IllegalArgumentException("Senha é obrigatória para cadastro de pacientes");
        }

        if (paciente.getUsername() == null || paciente.getUsername().isEmpty()) {
            paciente.setUsername(paciente.getEmail());
        }

        Set<Role> roles = EnumSet.of(Role.ROLE_PACIENTE);  // Define 'USER' como a role padrão
        paciente.setRoles(roles);

        Paciente pacienteSalvo = pacienteRepository.save(paciente);

        return PacienteMapper.toDTO(pacienteSalvo);
    }

    public void delete(Long id){

        if (!pacienteRepository.existsById(id)) {
            throw new ResourceAccessException("Paciente com ID " + id + " não encontrado.");
        }
        pacienteRepository.deleteById(id);
    }


    public PacienteDTO update(Long id, PacienteDTO dto){
        try{
            Paciente entity = pacienteRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com matrícula: " + id));
            updateDataFromDTO(entity, dto);
            Paciente pacienteAtualizado = pacienteRepository.save(entity);
            return PacienteMapper.toDTO(pacienteAtualizado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o Paciente: " + e.getMessage(), e);
        }
    }

    private void updateDataFromDTO(Paciente entity, PacienteDTO dto) {
        entity.setNome(dto.nome());
        entity.setCpf(dto.cpf());
        entity.setEmail(dto.email());
        entity.setTelefone(dto.telefone());
        if (dto.password() != null && !dto.password().isBlank()) {
            if (!dto.password().startsWith("$2a$")) {
                entity.setPassword(passwordEncoder.encode(dto.password()));
            }
        }


    }



}


