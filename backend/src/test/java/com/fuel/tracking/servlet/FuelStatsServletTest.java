package com.fuel.tracking.servlet;

import com.fuel.tracking.model.Car;
import com.fuel.tracking.service.CarService;
import com.fuel.tracking.service.FuelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FuelStatsServletTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarService carService;

    @Autowired
    private FuelService fuelService;

    private Long carId;

    @BeforeEach
    void setUp() {
        // Create a test car
        Car car = new Car("Test", "ServletCar", 2023);
        car = carService.createCar(car);
        carId = car.getId();
    }

    @Test
    void testServlet_ValidCarId_ReturnsStats() throws Exception {
        mockMvc.perform(get("/servlet/fuel-stats")
                .param("carId", carId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalFuelLiters").value(0.0));
    }

    @Test
    void testServlet_MissingCarId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/servlet/fuel-stats"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testServlet_InvalidCarId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/servlet/fuel-stats")
                .param("carId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testServlet_InvalidCarIdFormat_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/servlet/fuel-stats")
                .param("carId", "not-a-number"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.success").value(false));
    }
}