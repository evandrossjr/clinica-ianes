package com.sistema.clinica.controllers.api;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.dtos.PacienteDTO;
import com.sistema.clinica.services.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Lista todos os pacientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping()
    public ResponseEntity<List<PacienteDTO>> findAll() {
        List<PacienteDTO> list = pacienteService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @Operation(summary = "Busca paciente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<PacienteDTO> findById(@PathVariable Long id) {
        PacienteDTO obj = pacienteService.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @Operation(summary = "Cadastra um novo paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody PacienteDTO obj) {
        try{
            obj = pacienteService.insert(obj);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(obj.nome())
                    .toUri();
            return ResponseEntity.created(uri).body(obj);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @Operation(summary = "Exclui um paciente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza um paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<PacienteDTO> update(@PathVariable Long id, @RequestBody PacienteDTO obj) {
        obj = pacienteService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }
}
