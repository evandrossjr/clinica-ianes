package com.sistema.clinica.services;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.dtos.PacienteDTO;
import com.sistema.clinica.models.dtos.mappers.PacienteMapper;
import com.sistema.clinica.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @InjectMocks
    private PacienteService pacienteService;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Paciente paciente;

    @BeforeEach
    public void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Joana Souza");
        paciente.setEmail("joana@clinica.com");
        paciente.setPassword("senhaPaciente");
    }

    @Test
    public void testCreatePaciente() {
        Paciente paciente = new Paciente();
        paciente.setNome("João");
        paciente.setCpf("12345678901");
        paciente.setEmail("joao@example.com");

        Mockito.when(pacienteRepository.save(Mockito.any(Paciente.class))).thenReturn(paciente);

        PacienteDTO createdPaciente = pacienteService.insert(PacienteMapper.toDTO(paciente));
        assertNotNull(createdPaciente);
        assertEquals("João", createdPaciente.nome());
    }

    @Test
    public void deveRetornarTodosPacientes() {
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));

        List<PacienteDTO> result = pacienteService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Joana Souza", result.get(0).nome());
    }

    @Test
    public void deveRetornarPacientePorId() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteDTO result = pacienteService.findById(1L);

        assertNotNull(result);
        assertEquals("Joana Souza", result.nome());
    }

    @Test
    public void deveLancarExcecaoQuandoPacienteNaoEncontrado() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceAccessException.class, () -> pacienteService.findById(1L));
    }

    @Test
    public void deveInserirPaciente() {
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        when(passwordEncoder.encode("senhaPaciente")).thenReturn("senhaCriptografada");

        PacienteDTO result = pacienteService.insert(PacienteMapper.toDTO(paciente));

        assertNotNull(result);
        assertEquals("senhaCriptografada", result.password());
    }

    @Test
    public void deveExcluirPaciente() {
        doNothing().when(pacienteRepository).deleteById(1L);

        pacienteService.delete(1L);

        verify(pacienteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deveAtualizarPaciente() {
        Paciente updatedPaciente = new Paciente();
        updatedPaciente.setNome("Joana Souza Atualizada");
        
        PacienteDTO updatedPacienteDTO = PacienteMapper.toDTO(updatedPaciente);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(updatedPaciente);

        PacienteDTO result = pacienteService.update(1L, updatedPacienteDTO);

        assertEquals("Joana Souza Atualizada", result.nome());
    }
}
