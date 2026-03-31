package com.ocooldev.orders.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//# Configuração do Spring Security
@Configuration // -> Indica que esta classe define beans de configuração para o Spring
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    // Injeção do filtro JWT que criamos anteriormente
    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. Desabilita CSRF (não precisamos em APIs REST que usam JWT)
        http.csrf(csrf -> csrf.disable())
                // 2. Define regras de autorização para os endpoints
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                                // -> Esses endpoints ficam liberados (documentação e métricas)
                                .requestMatchers("/orders/**").authenticated()
                                // -> Endpoints de pedidos exigem autenticação JWT
                                .anyRequest().permitAll()
                        // -> Qualquer outro endpoint fica liberado
                )
                // 3. Adiciona o filtro JWT antes do filtro padrão de autenticação
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        // 4. Constrói e retorna a cadeia de filtros configurada
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Expõe o AuthenticationManager como bean, útil para autenticação programática
        return config.getAuthenticationManager();
    }
}
