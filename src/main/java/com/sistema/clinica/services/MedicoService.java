package com.sistema.clinica.services;

import java.util.*;

import com.sistema.clinica.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.repositories.MedicoRepository;

import jakarta.persistence.EntityNotFoundException;
@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    private final PasswordEncoder passwordEncoder;

    public MedicoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public List<Medico> findAll(){
        return medicoRepository.findAll();
    }

    public Medico findById(Long id){
        Optional<Medico> obj = medicoRepository.findById(id);
        return obj.orElseThrow(()-> new ResourceAccessException("Medico não encontrado"));
    }

    public Medico insert(Medico obj){
        if (medicoRepository.existsByEmail(obj.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (medicoRepository.existsByCrm(obj.getCrm())) {
            throw new IllegalArgumentException("CRM já cadastrado.");
        }
        if (medicoRepository.existsByUsername(obj.getUsername())) {
            throw new IllegalArgumentException("Médico já cadastrado.");
        }
        if (medicoRepository.existsByCpf(obj.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }



        String senhaCriptografada = passwordEncoder.encode(obj.getPassword());
        obj.setPassword(senhaCriptografada);

        if (obj.getUsername() == null || obj.getUsername().isEmpty()) {
            obj.setUsername(obj.getEmail());
        }

        Set<Role> roles = EnumSet.of(Role.ROLE_MEDICO);  // Define 'USER' como a role padrão
        obj.setRoles(roles);
        return medicoRepository.save(obj);
    }

    public void delete(Long id){
        medicoRepository.deleteById(id);
    }

    public Medico update(Long id, Medico obj){
        try{
            Medico entity = medicoRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Medico não encontrado com matrícula: " + id));
            updateData(entity, obj);
            if (medicoRepository.existsByEmail(obj.getEmail()) && !obj.getEmail().equals(entity.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }

            if (medicoRepository.existsByEmail(obj.getEmail()) && !Objects.equals(obj.getEmail(), entity.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }


            if (medicoRepository.existsByUsername(obj.getUsername()) && !obj.getUsername().equals(entity.getUsername())) {
                throw new IllegalArgumentException("Médico já cadastrado.");
            }

            if (medicoRepository.existsByCpf(obj.getCpf()) && !obj.getCpf().equals(entity.getCpf())) {
                throw new IllegalArgumentException("CPF já cadastrado.");
            }
            return medicoRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a medico: " + e.getMessage(), e);
        }
    }

    private void updateData(Medico entity, Medico obj) {
        entity.setCrm(obj.getCrm());
        entity.setNome(obj.getNome());
        entity.setCpf(obj.getCpf());
        entity.setEmail(obj.getEmail());
        entity.setTelefone(obj.getTelefone());
        entity.setEspecialidade(obj.getEspecialidade());
        entity.setDiasDisponiveis(obj.getDiasDisponiveis());
    }


    public List<Medico> buscarPorEspecialidade(String especialidade) {
        return medicoRepository.findByEspecialidade(especialidade);
    }
}



