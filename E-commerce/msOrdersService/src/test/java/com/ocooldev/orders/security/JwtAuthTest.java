package com.ocooldev.orders.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // -> Sobe o contexto completo do Spring Boot para o teste
@AutoConfigureMockMvc // -> Configura o MockMvc para simular requisições HTTP
class JwtAuthTest {

    @Autowired
    private MockMvc mockMvc; // -> Ferramenta para simular chamadas HTTP sem precisar subir servidor real

    @Test
    void testCreateOrderWithoutTokenShouldFail() throws Exception {
        // Simula uma requisição POST para /orders sem enviar JWT
        mockMvc.perform(post("/orders"))
                .andExpect(status().isForbidden());
        // Espera que o status seja 403 (Forbidden), já que o endpoint exige autenticação
    }

    @Test
    void testCreateOrderWithValidTokenShouldPass() throws Exception {
        // Aqui usamos um token "mockado". Em produção, você geraria um token real via JwtUtil.
        String token = "Bearer <jwt-valido>";

        // Simula uma requisição POST para /orders enviando o header Authorization com JWT
        mockMvc.perform(post("/orders")
                        .header("Authorization", token))
                .andExpect(status().isOk());
        // Espera que o status seja 200 (OK), já que o token foi enviado
    }
}
