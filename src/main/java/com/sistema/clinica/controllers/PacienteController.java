package com.sistema.clinica.controllers;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;


    @GetMapping()
    public ResponseEntity<List<Paciente>> findAll() {
        List<Paciente> list = pacienteService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Paciente> findById(@PathVariable Long id) {
        Paciente obj = pacienteService.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Paciente> insert(@RequestBody Paciente obj) {
        obj = pacienteService.insert(obj);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).body(obj);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Paciente> update(@PathVariable Long id, @RequestBody Paciente obj) {
        obj = pacienteService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }
}
