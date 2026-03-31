package com.ocooldev.orders.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;   // para @Value
import java.nio.charset.StandardCharsets;                   // para StandardCharsets

import java.security.Key;
import java.util.Date;

@Component // -> Torna a classe um componente gerenciado pelo Spring (pode ser injetada em outros lugares)
public class JwtUtil {

    private final Key SECRET_KEY;       // -> Chave secreta usada para assinar e validar os tokens JWT
    private final long EXPIRATION_TIME; // -> Tempo de expiração do token em milissegundos

    // Construtor: recebe os valores configurados no application.yml/properties
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        // Converte a string em uma chave segura para HS256 (precisa ter >= 32 caracteres)
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.EXPIRATION_TIME = expiration;
    }

    // Gera um token JWT para o usuário informado
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // -> Define o "dono" do token (subject)
                .setIssuedAt(new Date()) // -> Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // -> Data de expiração
                .signWith(SECRET_KEY) // -> Assina com a chave secreta
                .compact(); // -> Constrói o token final (string)
    }

    // Extrai o username (subject) de dentro do token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // -> Usa a mesma chave para validar
                .build()
                .parseClaimsJws(token) // -> Faz o parse e valida assinatura/expiração
                .getBody()
                .getSubject(); // -> Retorna o subject (username)
    }

    // Valida se o token é bem formado e não expirou
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // -> Usa a chave para validar
                    .build()
                    .parseClaimsJws(token); // -> Se não lançar exceção, token é válido
            return true;
        } catch (JwtException e) {
            // -> Qualquer erro (token inválido, expirado, assinatura incorreta) retorna false
            return false;
        }
    }
}
