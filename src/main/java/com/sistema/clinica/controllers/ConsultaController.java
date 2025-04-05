package com.sistema.clinica.controllers;

import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping()
    public ResponseEntity<List<Consulta>> findAll() {
        List<Consulta> list = consultaService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Consulta> findById(@PathVariable Long id) {
        Consulta obj = consultaService.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Consulta> insert(@RequestBody Consulta obj) {
        obj = consultaService.insert(obj);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).body(obj);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consultaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Consulta> update(@PathVariable Long id, @RequestBody Consulta obj) {
        obj = consultaService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

}
