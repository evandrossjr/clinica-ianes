package com.sistema.clinica.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.repositories.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PacienteService {


    @Autowired
    private PacienteRepository pacienteRepository ;


    public List<Paciente> findAll(){
        return pacienteRepository.findAll();
    }

    public Paciente findById(Long id){
        Optional<Paciente> obj = pacienteRepository.findById(id);
        return obj.orElseThrow(()-> new ResourceAccessException("Paciente não encontrado"));
    }

    public Paciente insert(Paciente obj){
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

    }


}


