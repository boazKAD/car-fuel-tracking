import java.net.http.*;
import java.net.URI;
import java.time.Duration;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("=== Testing Connection to Backend ===");

        try {
            // Test 1: Simple GET request
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/cars"))
                    .GET()
                    .build();

            System.out.println("Sending GET request to: http://localhost:8080/api/cars");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("✅ Connection Successful!");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

        } catch (Exception e) {
            System.out.println("❌ Connection Failed!");
            System.out.println("Error: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}