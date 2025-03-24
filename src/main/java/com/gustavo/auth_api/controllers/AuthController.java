package com.gustavo.auth_api.controllers;


import com.gustavo.auth_api.domain.entity.User;
import com.gustavo.auth_api.dto.LoginRequestDTO;
import com.gustavo.auth_api.dto.RegisterRequestDTO;
import com.gustavo.auth_api.dto.ResponseDTO;
import com.gustavo.auth_api.infra.security.TokenService;
import com.gustavo.auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found")); // Busca o usuário no banco de dados pelo e-mail e caso não encontre, lança uma exceção
        if(passwordEncoder.matches(body.password(), user.getPassword())){ // Verifica se a senha informada no corpo da requisição é válida
            String token = this.tokenService.GenerateToken(user); // Gera um token JWT para o usuário
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token)); // Retorna um status 200 com o nome do usuário e o token gerado
        }
        return ResponseEntity.badRequest().build(); // Retorna um status 400 caso a senha não seja válida
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<User> user = this.repository.findByEmail(body.email()); // Busca um usuário no banco de dados pelo e-mail, verifica se o usuário já existe

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));

            this.repository.save(newUser);

            String token = this.tokenService.GenerateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
