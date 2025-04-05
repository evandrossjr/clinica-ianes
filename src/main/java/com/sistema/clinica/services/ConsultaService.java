package com.sistema.clinica.services;

import com.sistema.clinica.models.Cliente;
import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.repositories.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<Consulta> findAll(){
        return consultaRepository.findAll();
    }

    public Consulta findById(Long id){
        Optional<Consulta> obj = consultaRepository.findById(id);
        return obj.orElseThrow(()-> new ResourceAccessException("Consulta não encontrado"));
    }

    public Consulta insert(Consulta obj){
        return consultaRepository.save(obj);
    }

    public void delete(Long id){
        consultaRepository.deleteById(id);
    }

    public Consulta update(Long id, Consulta obj){
        try{
            Consulta entity = consultaRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Consulta não encontrado com matrícula: " + id));
            updateData(entity, obj);
            return consultaRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a consulta: " + e.getMessage(), e);
        }
    }

    private void updateData(Consulta entity, Consulta obj) {
        entity.setMetodoPagamento(obj.getMetodoPagamento());

    }




}
