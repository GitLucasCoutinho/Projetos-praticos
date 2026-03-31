package com.ocooldev.orders.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

//# Serviço para carregar usuários (se necessário)
@Service // -> Anotação que registra essa classe como um "bean" do Spring, disponível para injeção
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Esse método é chamado pelo Spring Security quando precisa autenticar um usuário.
        // Normalmente, aqui você buscaria o usuário no banco de dados (ex.: via UserRepository).
        // Se o usuário não existir, você lança UsernameNotFoundException.

        // Exemplo simples: cria um usuário "fake" apenas com o username recebido.
        // O segundo parâmetro seria a senha (aqui está vazio, pois não estamos validando senha).
        // O terceiro parâmetro são as "authorities" (roles/permissões), aqui deixamos vazio.
        return new User(username, "", Collections.emptyList());
    }
}
