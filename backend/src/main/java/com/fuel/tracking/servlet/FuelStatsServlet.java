package com.fuel.tracking.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.tracking.dto.ApiResponse;
import com.fuel.tracking.dto.FuelStatsResponse;
import com.fuel.tracking.model.FuelStats;
import com.fuel.tracking.service.CarService;
import com.fuel.tracking.service.FuelService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Manual Servlet implementation to demonstrate HTTP request lifecycle
 * Endpoint: GET /servlet/fuel-stats?carId={id}
 */
@WebServlet("/servlet/fuel-stats")
public class FuelStatsServlet extends HttpServlet {

    // We need to inject Spring beans into Servlet
    // Since Servlet is not managed by Spring, we'll use a different approach
    private FuelService fuelService;
    private ObjectMapper objectMapper;

    /**
     * Servlet initialization - get Spring beans from context
     */
    @Override
    public void init() throws ServletException {
        super.init();
        initializeBeans();
    }

    /**
     * Initialize Spring beans from context (can be called lazily)
     */
    private void initializeBeans() {
        if (fuelService != null && objectMapper != null) {
            return; // Already initialized
        }

        // Get Spring context to access beans
        var springContext = (org.springframework.web.context.WebApplicationContext) getServletContext().getAttribute(
                org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        if (springContext != null) {
            fuelService = springContext.getBean(FuelService.class);
            objectMapper = springContext.getBean(ObjectMapper.class);
        }
    }

    /**
     * Handle GET requests
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ensure servlet is initialized
        initializeBeans();
        
        if (fuelService == null || objectMapper == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write("{\"success\":false,\"error\":\"Servlet initialization failed\"}");
            writer.flush();
            writer.close();
            return;
        }

        // Manually parse query parameters (as required)
        String carIdParam = request.getParameter("carId");

        // Set Content-Type explicitly (as required)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            // Validate carId parameter
            if (carIdParam == null || carIdParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                ApiResponse<Object> errorResponse = ApiResponse.error(
                        "Missing required parameter: carId");
                writer.write(objectMapper.writeValueAsString(errorResponse));
                return;
            }

            // Parse carId (manual parsing as required)
            Long carId;
            try {
                carId = Long.parseLong(carIdParam);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                ApiResponse<Object> errorResponse = ApiResponse.error(
                        "Invalid carId format. Must be a number.");
                writer.write(objectMapper.writeValueAsString(errorResponse));
                return;
            }

            // Use the same Service layer instance as REST API (as required)
            FuelStats stats = fuelService.calculateFuelStats(carId);

            // Convert to response DTO
            FuelStatsResponse statsResponse = new FuelStatsResponse(
                    stats.getTotalFuelLiters(),
                    stats.getTotalCost(),
                    stats.getAverageConsumption(),
                    stats.getTotalEntries(),
                    stats.getAveragePricePerLiter(),
                    stats.getCostPer100km());

            // Set status code explicitly (as required)
            response.setStatus(HttpServletResponse.SC_OK); // 200

            // Create success response
            ApiResponse<FuelStatsResponse> apiResponse = ApiResponse.success(statsResponse);

            // Write JSON response manually
            writer.write(objectMapper.writeValueAsString(apiResponse));

        } catch (CarService.CarNotFoundException e) {
            // Handle car not found - set status code explicitly
            if (writer != null && objectMapper != null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                ApiResponse<Object> errorResponse = ApiResponse.error(
                        "Car not found with id: " + carIdParam);
                writer.write(objectMapper.writeValueAsString(errorResponse));
            }

        } catch (IllegalArgumentException e) {
            // Handle invalid input
            if (writer != null && objectMapper != null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                ApiResponse<Object> errorResponse = ApiResponse.error(
                        "Invalid input: " + e.getMessage());
                writer.write(objectMapper.writeValueAsString(errorResponse));
            }

        } catch (Exception e) {
            // Handle any other exceptions
            if (writer != null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                if (objectMapper != null) {
                    ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Internal server error");
                    writer.write(objectMapper.writeValueAsString(errorResponse));
                } else {
                    writer.write("{\"success\":false,\"error\":\"Internal server error\"}");
                }
            }

            // Log the exception (in real app, use proper logging)
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Only GET is supported for this endpoint
        initializeBeans();
        
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405
        resp.setContentType("application/json");

        ApiResponse<Object> errorResponse = ApiResponse.error(
                "Method not allowed. Only GET is supported.");

        PrintWriter writer = resp.getWriter();
        if (objectMapper != null) {
            writer.write(objectMapper.writeValueAsString(errorResponse));
        } else {
            writer.write("{\"success\":false,\"error\":\"Method not allowed. Only GET is supported.\"}");
        }
        writer.flush();
        writer.close();
    }
}