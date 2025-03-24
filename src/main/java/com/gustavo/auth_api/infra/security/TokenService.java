package com.gustavo.auth_api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gustavo.auth_api.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String GenerateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("login-auth-api") // nome da aplicação que está gerando o token
                    .withSubject(user.getEmail()) // para quem o token foi gerado, salvar o email do usuário no token
                    .withExpiresAt(this.generateExpirationDate()) // data de expiração do token
                    .sign(algorithm); // assinar o token com o algoritmo HMAC256

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error creating token");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token) // verifica se o token é válido
                    .getSubject(); // retorna o email do usuário que está no token
        } catch (JWTVerificationException e) {
           return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); // 2 horas de expiração, -03:00 é o fuso horário de Brasília
    }
}
