package com.sistema.clinica.controllers.api;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.dtos.FuncionarioDTO;
import com.sistema.clinica.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;


    @GetMapping()
    public ResponseEntity<List<FuncionarioDTO>> findAll() {
        List<FuncionarioDTO> list = funcionarioService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<FuncionarioDTO> findById(@PathVariable Long id) {
        FuncionarioDTO obj = funcionarioService.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<FuncionarioDTO> insert(@RequestBody FuncionarioDTO obj) {
        obj = funcionarioService.insert(obj);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.id())
                .toUri();
        return ResponseEntity.created(uri).body(obj);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<FuncionarioDTO> update(@PathVariable Long id, @RequestBody FuncionarioDTO obj) {
        obj = funcionarioService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }
}
