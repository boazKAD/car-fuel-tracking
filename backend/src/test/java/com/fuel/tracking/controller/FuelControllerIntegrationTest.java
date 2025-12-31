package com.fuel.tracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.tracking.dto.AddFuelRequest;
import com.fuel.tracking.dto.CreateCarRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FuelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long carId;

    @BeforeEach
    void setUp() throws Exception {
        // Create a car first
        CreateCarRequest carRequest = new CreateCarRequest();
        carRequest.setBrand("Honda");
        carRequest.setModel("Civic");
        carRequest.setYear(2021);

        MvcResult result = mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        carId = objectMapper.readTree(response)
                .path("data")
                .path("id")
                .asLong();
    }

    @Test
    void addFuelEntry_ValidRequest_ReturnsCreated() throws Exception {
        AddFuelRequest request = new AddFuelRequest();
        request.setLiters(40.0);
        request.setPrice(1.5);
        request.setOdometer(10000);

        mockMvc.perform(post("/api/cars/{carId}/fuel", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.liters").value(40.0))
                .andExpect(jsonPath("$.data.pricePerLiter").value(1.5))
                .andExpect(jsonPath("$.data.totalCost").value(60.0))
                .andExpect(jsonPath("$.data.odometer").value(10000));
    }

    @Test
    void addFuelEntry_InvalidCarId_ReturnsNotFound() throws Exception {
        AddFuelRequest request = new AddFuelRequest();
        request.setLiters(40.0);
        request.setPrice(1.5);
        request.setOdometer(10000);

        mockMvc.perform(post("/api/cars/{carId}/fuel", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getFuelStats_ReturnsStats() throws Exception {
        // Add two fuel entries to calculate stats
        AddFuelRequest request1 = new AddFuelRequest();
        request1.setLiters(40.0);
        request1.setPrice(1.5);
        request1.setOdometer(10000);

        AddFuelRequest request2 = new AddFuelRequest();
        request2.setLiters(35.0);
        request2.setPrice(1.6);
        request2.setOdometer(10600);

        mockMvc.perform(post("/api/cars/{carId}/fuel", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/api/cars/{carId}/fuel", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        mockMvc.perform(get("/api/cars/{carId}/fuel/stats", carId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalFuelLiters").value(75.0))
                .andExpect(jsonPath("$.data.totalCost").value(40 * 1.5 + 35 * 1.6))
                .andExpect(jsonPath("$.data.totalEntries").value(2))
                .andExpect(jsonPath("$.data.averageConsumption").exists());
    }
    // @Test
    // void debugTest() throws Exception {
    // // Test with minimal request
    // String requestJson = "{\"liters\":40.0,\"price\":1.5,\"odometer\":10000}";

    // System.out.println("Testing with JSON: " + requestJson);

    // mockMvc.perform(post("/api/cars/{carId}/fuel", carId)
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(requestJson))
    // .andDo(Print()) // Print the response for debugging
    // .andExpect(status().isCreated());
    // }
}