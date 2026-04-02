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

/**
 * Filtro JWT que intercepta todas as requisições HTTP e valida o token.
 * Extende OncePerRequestFilter para garantir execução única por requisição.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Injeção do utilitário responsável por validar e extrair informações do token
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Define quais endpoints NÃO devem passar pelo filtro JWT.
     * Isso evita que Swagger, Actuator e outros endpoints públicos sejam bloqueados.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator");
    }

    /**
     * Lógica principal do filtro:
     * - Extrai o header Authorization
     * - Valida o token JWT
     * - Se válido, registra o usuário no contexto de segurança
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // Extrai o header "Authorization"
        String authHeader = request.getHeader("Authorization");

        // Verifica se existe e começa com "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Valida o token
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);

                // Cria objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                // Adiciona detalhes da requisição (IP, sessão, etc.)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Registra o usuário autenticado no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua o fluxo da requisição
        chain.doFilter(request, response);
    }
}