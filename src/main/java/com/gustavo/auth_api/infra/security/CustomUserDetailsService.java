package com.gustavo.auth_api.infra.security;

import com.gustavo.auth_api.domain.entity.User;
import com.gustavo.auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found")); // Busca o usuário no banco de dados pelo e-mail e caso não encontre, lança uma exceção
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>()); // Retorna um objeto do tipo UserDetails, que é o objeto que o Spring Security utiliza para representar um usuário.
    }
}
