package com.ocooldev.orders.controller;

import com.ocooldev.orders.dto.AuthRequestDTO;
import com.ocooldev.orders.security.JwtUtil;
import com.ocooldev.orders.security.AuthMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Login do usuário",
            description = "Recebe username e password e retorna um token JWT válido."
    )
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido, retorna token JWT")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDTO request) {
        try {
            String token = jwtUtil.generateToken(request.getUsername());
            authMetrics.incrementLoginSuccess();
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            authMetrics.incrementLoginFailure();
            return ResponseEntity.status(401).body("Login falhou");
        }
    }
}
