package com.sistema.clinica.services;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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



    public List<Paciente> findAll(){
        return pacienteRepository.findAll();
    }

    public Paciente findById(Long id){
        Optional<Paciente> obj = pacienteRepository.findById(id);
        return obj.orElseThrow(()-> new ResourceAccessException("Paciente não encontrado"));
    }

    public Paciente insert(Paciente obj){

        String senhaCriptografada = passwordEncoder.encode(obj.getPassword());
        obj.setPassword(senhaCriptografada);

        if (obj.getUsername() == null || obj.getUsername().isEmpty()) {
            obj.setUsername(obj.getEmail());
        }

        Set<Role> roles = EnumSet.of(Role.ROLE_PACIENTE);  // Define 'USER' como a role padrão
        obj.setRoles(roles);
        return pacienteRepository.save(obj);
    }

    public void delete(Long id){
        pacienteRepository.deleteById(id);
    }

    public Paciente update(Long id, Paciente obj){
        try{
            Paciente entity = pacienteRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com matrícula: " + id));
            updateData(entity, obj);
            return pacienteRepository.save(entity);
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


