package com.sistema.clinica.services;

import com.sistema.clinica.models.Medico;
import com.sistema.clinica.repositories.MedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Adicionando a extensão do Mockito para JUnit 5
public class MedicoServiceTest {

    @InjectMocks
    private MedicoService medicoService;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Medico medico;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuração inicial para o Medico
        medico = new Medico();
        medico.setId(1L);
        medico.setNome("Dr. João");
        medico.setCrm(123456);
        medico.setCpf("123.456.789-00");
        medico.setEmail("joao@clinica.com");
        medico.setTelefone("123456789");
        medico.setEspecialidade("Cardiologia");
        medico.setUsername("joao");
        medico.setPassword("password123");
        medico.setRoles(Set.of());
    }

    @Test
    public void deveRetornarTodosMedicos() {
        List<Medico> medicos = List.of(medico);
        when(medicoRepository.findAll()).thenReturn(medicos);

        List<Medico> result = medicoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. João", result.get(0).getNome());
    }

    @Test
    public void deveRetornarMedicoPorId() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        Medico result = medicoService.findById(1L);

        assertNotNull(result);
        assertEquals("Dr. João", result.getNome());
    }

    @Test
    public void deveLancarErroAoBuscarMedicoInexistente() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceAccessException.class, () -> medicoService.findById(1L));

        assertEquals("Medico não encontrado", exception.getMessage());
    }

    @Test
    public void deveInserirMedico() {
        when(passwordEncoder.encode(medico.getPassword())).thenReturn("senhaCriptografada");
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        Medico result = medicoService.insert(medico);

        assertNotNull(result);
        assertEquals("senhaCriptografada", result.getPassword());
        verify(medicoRepository, times(1)).save(medico);
    }

    @Test
    public void deveAtualizarMedico() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        Medico updatedMedico = new Medico();
        updatedMedico.setNome("Dr. João Atualizado");
        updatedMedico.setCrm(654321);

        Medico result = medicoService.update(1L, updatedMedico);

        assertNotNull(result);
        assertEquals("Dr. João Atualizado", result.getNome());
        assertEquals("654321", result.getCrm());
        verify(medicoRepository, times(1)).save(medico);
    }

    @Test
    public void deveDeletarMedico() {
        doNothing().when(medicoRepository).deleteById(1L);

        medicoService.delete(1L);

        verify(medicoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deveBuscarMedicosPorEspecialidade() {
        List<Medico> medicos = List.of(medico);
        when(medicoRepository.findByEspecialidade("Cardiologia")).thenReturn(medicos);

        List<Medico> result = medicoService.buscarPorEspecialidade("Cardiologia");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardiologia", result.get(0).getEspecialidade());
    }
}
