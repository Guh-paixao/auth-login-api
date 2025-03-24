package com.gustavo.auth_api.infra.security;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita o CSRF (Cross-Site Request Forgery) para permitir requisições de outros domínios (CORS) sem a necessidade de um token.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura a política de criação de sessão para STATELESS, API REST não mantém estado
                .authorizeHttpRequests(authorize -> authorize // Configura as autorizações
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permite o acesso ao endpoint /auth/login para todos
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite o acesso ao endpoint /auth/register para todos
                        .anyRequest().authenticated() // Qualquer outra requisição precisa de autenticação
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();} // Cria um bean do tipo PasswordEncoder para criptografar as senhas dos usuários

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception { // Cria um bean do tipo AuthenticationManager para gerenciar a autenticação dos usuários, utilizando a configuração de autenticação do Spring Security
        return authenticationConfiguration.getAuthenticationManager();
    }


}
