package frontend.controller;

import frontend.dto.UserDTO;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthController {

    private final ApiClient client = ApiClient.getInstance();

    public boolean login(String email, String password) {
        try {
            UserDTO loginRequest = new UserDTO(email, password);
            String jsonBody = client.getObjectMapper().writeValueAsString(loginRequest);


            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));


            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {
                AuthResponse authResponse = client.getObjectMapper().readValue(response.body(), AuthResponse.class);

                client.setJwtToken(authResponse.getToken());

                System.out.println("Login OK. Token salvato globalmente.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class AuthResponse {

        private String token;

        public AuthResponse() {}

        public String getToken() { return token; }

        public void setToken(String token) { this.token = token; }
    }
}