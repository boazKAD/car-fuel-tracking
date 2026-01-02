package com.fuel.tracking.cli.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.tracking.cli.dto.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        // Register Java 8 time module for LocalDateTime
        objectMapper.findAndRegisterModules();
    }

    public ApiClient() {
        this("http://localhost:8080");
    }

    /**
     * Create a new car
     */
    public CarResponse createCar(CreateCarCommand command) throws ApiClientException {
        try {
            // Convert to JSON
            String jsonBody = objectMapper.writeValueAsString(command);

            // Create request
            String url = baseUrl + "api/cars";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // Check for HTTP errors
            if (response.statusCode() >= 400) {
                throw new ApiClientException("HTTP error: " + response.statusCode());
            }

            // Parse response with proper type
            ApiResponse<CarResponse> apiResponse = objectMapper.readValue(
                    response.body(),
                    objectMapper.getTypeFactory().constructParametricType(
                            ApiResponse.class, CarResponse.class));

            if (!apiResponse.isSuccess()) {
                throw new ApiClientException("API returned error: " +
                        (apiResponse.getError() != null ? apiResponse.getError() : apiResponse.getMessage()));
            }

            return apiResponse.getData();

        } catch (ApiClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiClientException("Error creating car: " + e.getMessage(), e);
        }
    }

    /**
     * Add fuel entry to a car
     */
    public void addFuel(AddFuelCommand command) throws ApiClientException {
        try {
            // Create request body without carId (carId is in URL path)
            // Backend expects: { liters, price, odometer }
            java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
            requestBody.put("liters", command.getLiters());
            requestBody.put("price", command.getPrice());
            requestBody.put("odometer", command.getOdometer());
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "api/cars/" + command.getCarId() + "/fuel"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // Check for HTTP errors
            if (response.statusCode() >= 400) {
                throw new ApiClientException("HTTP error: " + response.statusCode());
            }

            ApiResponse<?> apiResponse = objectMapper.readValue(
                    response.body(), ApiResponse.class);

            if (!apiResponse.isSuccess()) {
                throw new ApiClientException("Failed to add fuel: " +
                        (apiResponse.getError() != null ? apiResponse.getError() : apiResponse.getMessage()));
            }

        } catch (ApiClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiClientException("Error adding fuel: " + e.getMessage(), e);
        }
    }

    /**
     * Get fuel statistics for a car
     */
    public FuelStatsResponse getFuelStats(Long carId) throws ApiClientException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "api/cars/" + carId + "/fuel/stats"))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // Check for HTTP errors
            if (response.statusCode() >= 400) {
                throw new ApiClientException("HTTP error: " + response.statusCode());
            }

            ApiResponse<FuelStatsResponse> apiResponse = objectMapper.readValue(
                    response.body(),
                    objectMapper.getTypeFactory().constructParametricType(
                            ApiResponse.class, FuelStatsResponse.class));

            if (!apiResponse.isSuccess()) {
                throw new ApiClientException("Failed to get fuel stats: " +
                        (apiResponse.getError() != null ? apiResponse.getError() : apiResponse.getMessage()));
            }

            return apiResponse.getData();

        } catch (ApiClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiClientException("Error getting fuel stats: " + e.getMessage(), e);
        }
    }

    /**
     * Test connection to backend
     */
    public boolean testConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "api/cars"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Custom exception for API client errors
     */
    public static class ApiClientException extends Exception {
        public ApiClientException(String message) {
            super(message);
        }

        public ApiClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}