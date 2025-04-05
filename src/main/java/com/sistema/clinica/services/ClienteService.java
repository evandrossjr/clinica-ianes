package com.sistema.clinica.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.sistema.clinica.models.Cliente;
import com.sistema.clinica.repositories.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {


    @Autowired
    private ClienteRepository clienteRepository ;


    public List<Cliente> findAll(){
        return clienteRepository.findAll();
    }

    public Cliente findById(Long id){
        Optional<Cliente> obj = clienteRepository.findById(id);
        return obj.orElseThrow(()-> new ResourceAccessException("Cliente não encontrado"));
    }

    public Cliente insert(Cliente obj){
        return clienteRepository.save(obj);
    }

    public void delete(Long id){
        clienteRepository.deleteById(id);
    }

    public Cliente update(Long id, Cliente obj){
        try{
            Cliente entity = clienteRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com matrícula: " + id));
            updateData(entity, obj);
            return clienteRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    private void updateData(Cliente entity, Cliente obj) {
        entity.setNome(obj.getNome());
        entity.setCpf(obj.getCpf());
        entity.setEmail(obj.getEmail());
        entity.setTelefone(obj.getTelefone());
        entity.setEndereco(obj.getEndereco());
    }


}


