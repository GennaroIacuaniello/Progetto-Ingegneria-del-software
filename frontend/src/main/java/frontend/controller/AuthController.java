package frontend.controller;

import frontend.dto.UserDTO;
import lombok.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("java:S6548")
public class AuthController {

    private static AuthController instance;

    private final ApiClient client = ApiClient.getInstance();

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Setter
    @Getter
    private UserDTO loggedUser;

    private AuthController(){

    }

    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    public boolean login(String email, String password) {

        try {
            LoginRequest loginRequest = new LoginRequest(email, password);

            String jsonBody = client.getObjectMapper().writeValueAsString(loginRequest);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                AuthResponse authResponse = client.getObjectMapper().readValue(response.body(), AuthResponse.class);

                client.setJwtToken(authResponse.getToken());
                this.loggedUser= authResponse.getUser();

                logger.log(Level.FINE, "Login OK. Ruolo: " + authResponse.getUser().getRole());

                return true;
            } else {

                logger.log(Level.WARNING, "Login fallito. Codice: {0}", response.statusCode());

                logger.log(Level.WARNING, "Dettaglio server: {0}", response.body());

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {

        private String email;
        private String password;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {

        private String token;
        private UserDTO user;

    }

    public boolean registration(String email, String password, int role) {

        try {
            RegisterRequest registerRequest = new RegisterRequest(email, password, role);

            String jsonBody = client.getObjectMapper().writeValueAsString(registerRequest);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                logger.log(Level.FINE,"Registration ok! Login to start working!");
                return true;

            } else {
                logger.log(Level.WARNING,"Registration failed. Error code: {0}", response.statusCode());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {

        private String email;
        private String password;
        private int role;

    }

}