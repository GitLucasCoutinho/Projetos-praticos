package com.ocooldev.orders.controller;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class) // carrega apenas a camada de controller
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrderWithInvalidCustomerId() throws Exception {
        // DTO inválido: customerId vazio
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCustomerId(""); // inválido
        dto.setTotalAmount(100.0);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()) // espera 400
                .andExpect(jsonPath("$.customerId").value("O customerId não pode ser vazioCOOL"));
    }
}
