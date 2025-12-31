package com.fuel.tracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.tracking.dto.CreateCarRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCar_ValidRequest_ReturnsCreated() throws Exception {
        CreateCarRequest request = new CreateCarRequest();
        request.setBrand("Toyota");
        request.setModel("Corolla");
        request.setYear(2020);

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Car created successfully"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.brand").value("Toyota"))
                .andExpect(jsonPath("$.data.model").value("Corolla"))
                .andExpect(jsonPath("$.data.year").value(2020));
    }

    @Test
    void createCar_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateCarRequest request = new CreateCarRequest();
        request.setBrand(""); // Empty brand

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").isString()) // Error is now a string
                .andExpect(jsonPath("$.error").value(containsString("Brand is required")));
    }

    @Test
    void getAllCars_ReturnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }
}