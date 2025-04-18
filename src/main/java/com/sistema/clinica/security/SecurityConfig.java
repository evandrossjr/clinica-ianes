package com.sistema.clinica.security;

import com.sistema.clinica.repositories.PessoaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    public UserDetailsService userDetailsService(PessoaRepository pessoaRepository) {
        return new PessoaDetailsService(pessoaRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**","/h2-console/**").permitAll() // permite acesso à página de login e recursos estáticos
                        .requestMatchers("/medico","/medico/**").hasAnyRole("MEDICO","ADMIN")
                        .requestMatchers("/funcionario","/funcionario/**").hasAnyRole("FUNCIONARIO","ADMIN")
                        .requestMatchers("/paciente","/paciente/**").hasAnyRole("PACIENTE","ADMIN")
                        .requestMatchers("/admin","admin/**").hasRole("ADMIN")
                        .requestMatchers("/index").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // rota que o usuário acessa
                        .loginProcessingUrl("/login") // rota que o form faz POST (deixa igual ao `th:action`)
                        .successHandler(new CustomAuthenticationSuccessHandler()) // redireciona ao logar com sucesso
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



