package com.alexandre.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Habilita a configuração personalizada de segurança no Spring Security
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    //Filtro personalizado para autenticação/validação de requisições
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Desabilita a proteção CSRF.
        http.csrf(csrf -> csrf.disable())
            //Define a política de criação de sessão como STATELESS.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                authorize -> authorize
                //Permite acesso público à rota de login.
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                //Permite acesso público à rota de registro.
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                //Exige autenticação para qualquer outra rota.
                .anyRequest().authenticated()
                //Adiciona o filtro personalizado antes do filtro padrão.
            ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        
        //Constrói e retorna a configuração de segurança.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //Define o codificador de senhas como BCrypt.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //Configura o gerenciador de autenticação.
        return authenticationConfiguration.getAuthenticationManager();
    }
    
}
