package com.sistema.clinica.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sistema.clinica.models.Medico;
import com.sistema.clinica.services.MedicoService;

@RestController
@RequestMapping(value = "/medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;
    
    @GetMapping()
    public ResponseEntity<List<Medico>> findAll() {
        List<Medico> list = medicoService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Medico> findById(@PathVariable Long id) {
        Medico obj = medicoService.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Medico> insert(@RequestBody Medico obj) {
        obj = medicoService.insert(obj);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).body(obj);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Medico> update(@PathVariable Long id, @RequestBody Medico obj) {
        obj = medicoService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

}


