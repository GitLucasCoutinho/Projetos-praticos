package com.ocooldev.orders.controller;

import com.ocooldev.orders.security.JwtUtil;
import com.ocooldev.orders.security.AuthMetrics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthMetrics authMetrics;

    public AuthController(JwtUtil jwtUtil, AuthMetrics authMetrics) {
        this.jwtUtil = jwtUtil;
        this.authMetrics = authMetrics;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username) {
        try {
            // Aqui você poderia validar usuário/senha no banco
            String token = jwtUtil.generateToken(username);

            // Registra login bem-sucedido
            authMetrics.incrementLoginSuccess();

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            // Registra falha de login
            authMetrics.incrementLoginFailure();
            return ResponseEntity.status(401).body("Login falhou");
        }
    }
}
