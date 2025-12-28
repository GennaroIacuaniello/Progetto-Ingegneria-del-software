package frontend.controller;

import frontend.dto.UserDTO;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthController {

    private static AuthController instance;
    private final ApiClient client = ApiClient.getInstance();

    private UserDTO loggedUser;

    private AuthController(){
        //Empty constructor needed for jackson
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

                System.out.println("Login OK. Ruolo: " + authResponse.getUser().getRole());
                return true;
            } else {
                System.err.println("Login fallito. Codice: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    public static class AuthResponse {
        private String token;
        private UserDTO user;

        public AuthResponse() {
            // Empty costructor needed for Jackson
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public UserDTO getUser() { return user; }
        public void setUser(UserDTO user) { this.user = user; }
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

                System.out.println("Registration ok! Login to start working!");
                return true;

            } else {
                System.err.println("Registration failed. Error code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class RegisterRequest {
        private String email;
        private String password;
        private int role;

        public RegisterRequest(String email, String password, int role) {
            this.email = email;
            this.password = password;
            this.role = role;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }


}