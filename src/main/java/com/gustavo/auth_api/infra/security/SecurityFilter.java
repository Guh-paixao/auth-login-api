package com.gustavo.auth_api.infra.security;

import com.gustavo.auth_api.domain.entity.User;
import com.gustavo.auth_api.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoveryToken(request);  // Recupera o token
        var login = tokenService.validateToken(token); // Valida o token, a validação retorna o e-mail do usuário.

        if(login != null) { // Se o token for inválido, a função de validação retorna null.
            User user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User Not Found")); // Busca o usuário no banco de dados pelo e-mail e lança uma exceção caso não encontre.
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // Cria a autoridade(role) do usuário;
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities); // Cria a autenticação do usuário
            SecurityContextHolder.getContext().setAuthentication(authentication); // Define a autenticação do usuário no contexto de segurança do Spring.
        }
    }

    private String recoveryToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization"); // pega o header de autorização
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); // Remove o prefixo Bearer
    }
}
