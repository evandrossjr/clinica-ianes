package com.sistema.clinica;

import com.sistema.clinica.models.Cliente;
import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.MetodoPagamento;
import com.sistema.clinica.repositories.ClienteRepository;
import com.sistema.clinica.repositories.ConsultaRepository;
import com.sistema.clinica.repositories.MedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public void run(String... args) throws Exception {

        Cliente c1 = new Cliente(null, "Maria","12345","maria@email.com", 7112345, null);
        Cliente c2 = new Cliente(null, "Joana","98765","joana@email.com", 7198765,null);
        Cliente c3 = new Cliente(null, "Ana","98765","ana@email.com", 7198765,null);

        clienteRepository.saveAll(Arrays.asList(c1,c2));
        clienteRepository.save(c3);

        Consulta ct1 = new Consulta(null, MetodoPagamento.DEBITO);
        Consulta ct2 = new Consulta(null, MetodoPagamento.DINHEIRO);
        Consulta ct3 = new Consulta(null, MetodoPagamento.CREDITO);

        consultaRepository.save(ct1);
        consultaRepository.saveAll(Arrays.asList(ct2,ct3));

        Medico m1 = new Medico(1020,null, "Fabio Alex", "123456789", "exemplo@hotmail.com", 95008400, null);
        
        medicoRepository.save(m1);

    }
}
