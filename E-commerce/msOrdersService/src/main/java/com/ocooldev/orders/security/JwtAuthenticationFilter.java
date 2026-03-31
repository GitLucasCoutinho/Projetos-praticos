package com.ocooldev.orders.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//# Filtro que valida o token JWT em cada requisição
@Component // -> Registra essa classe como um "bean" do Spring, para ser usada automaticamente
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Esse filtro é executado uma vez por requisição (por isso "OncePerRequestFilter")

    private final JwtUtil jwtUtil;

    // Injeção do utilitário JWT (responsável por gerar/validar tokens)
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // 1. Extrai o header "Authorization" da requisição
        String authHeader = request.getHeader("Authorization");

        // 2. Verifica se o header existe e começa com "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Remove o prefixo "Bearer " e pega só o token
            String token = authHeader.substring(7);

            // 3. Valida o token usando JwtUtil
            if (jwtUtil.validateToken(token)) {
                // Se válido, extrai o username do token
                String username = jwtUtil.extractUsername(token);

                // 4. Cria um objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                // Adiciona detalhes da requisição (IP, sessão, etc.)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Registra o usuário autenticado no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. Continua o fluxo da requisição (passa para o próximo filtro ou controller)
        chain.doFilter(request, response);
    }
}
