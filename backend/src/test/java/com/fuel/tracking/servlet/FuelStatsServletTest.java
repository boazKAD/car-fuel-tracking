package com.fuel.tracking.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.tracking.model.Car;
import com.fuel.tracking.repository.CarRepository;
import com.fuel.tracking.repository.InMemoryCarRepository;
import com.fuel.tracking.service.CarService;
import com.fuel.tracking.service.FuelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class FuelStatsServletTest {

    private FuelStatsServlet servlet;
    private CarService carService;
    private FuelService fuelService;
    private ObjectMapper objectMapper;
    private CarRepository carRepository;
    private Long carId;

    @BeforeEach
    void setUp() throws Exception {
        // Setup repositories and services
        carRepository = new InMemoryCarRepository();
        carService = new CarService(carRepository);
        fuelService = new FuelService(carRepository);
        objectMapper = new ObjectMapper();

        // Create a test car
        Car car = new Car("Test", "ServletCar", 2023);
        car = carService.createCar(car);
        carId = car.getId();

        // Setup servlet with Spring context
        servlet = new FuelStatsServlet();
        MockServletContext servletContext = new MockServletContext();
        
        // Create Spring WebApplicationContext
        GenericWebApplicationContext webApplicationContext = new GenericWebApplicationContext();
        webApplicationContext.getBeanFactory().registerSingleton("fuelService", fuelService);
        webApplicationContext.getBeanFactory().registerSingleton("objectMapper", objectMapper);
        webApplicationContext.refresh();
        
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
        
        MockServletConfig servletConfig = new MockServletConfig(servletContext);
        servlet.init(servletConfig);
    }

    @Test
    void testServlet_ValidCarId_ReturnsStats() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setParameter("carId", carId.toString());
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        servlet.doGet(request, response);
        
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("success") && responseBody.contains("true"), 
                "Response should contain success:true. Got: " + responseBody);
        assertTrue(responseBody.contains("totalFuelLiters") && responseBody.contains("0.0"),
                "Response should contain totalFuelLiters:0.0. Got: " + responseBody);
    }

    @Test
    void testServlet_MissingCarId_ReturnsBadRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        // No carId parameter
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        servlet.doGet(request, response);
        
        assertEquals(400, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("success") && responseBody.contains("false"),
                "Response should contain success:false. Got: " + responseBody);
        // Error response can have either "error" or "message" field
        assertTrue(responseBody.contains("error") || responseBody.contains("message"),
                "Response should contain error or message. Got: " + responseBody);
    }

    @Test
    void testServlet_InvalidCarId_ReturnsNotFound() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setParameter("carId", "999");
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        servlet.doGet(request, response);
        
        assertEquals(404, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));
        assertTrue(response.getContentAsString().contains("\"success\":false"));
    }

    @Test
    void testServlet_InvalidCarIdFormat_ReturnsBadRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setParameter("carId", "not-a-number");
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        servlet.doGet(request, response);
        
        assertEquals(400, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));
        assertTrue(response.getContentAsString().contains("\"success\":false"));
    }
}