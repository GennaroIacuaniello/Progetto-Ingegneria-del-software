package frontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import frontend.dto.UserDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AuthController {

        private static final String BASE_URL = "http://localhost:8080";
        private final HttpClient httpClient;
        private final ObjectMapper objectMapper; // IL CUORE DI JACKSON
        private String jwtToken = null;

        public AuthController() {
            this.httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            // Inizializziamo Jackson
            this.objectMapper = new ObjectMapper();
        }

        /**
         * Effettua il login e salva il JWT se successo.
         */
        public boolean login(String username, String password) {
            try {
                // 1. Creiamo l'oggetto Java con i dati
                UserDTO loginRequest = new UserDTO(username, password);

                // 2. SERIALIZZAZIONE: Jackson trasforma l'oggetto in stringa JSON
                // Output sicuro: {"username":"mario", "password":"pa\"ssword"}
                String jsonBody = objectMapper.writeValueAsString(loginRequest);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    // 3. DESERIALIZZAZIONE: Jackson trasforma la stringa JSON in oggetto Java
                    // Il JSON ricevuto {"token": "eyJh..."} diventa un oggetto AuthResponse
                    AuthResponse authResponse = objectMapper.readValue(response.body(), AuthResponse.class);

                    this.jwtToken = authResponse.getToken();
                    System.out.println("Login OK. Token ricevuto.");
                    return true;
                } else {
                    System.out.println("Login Fallito. Status: " + response.statusCode());
                    System.out.println("Risposta server: " + response.body());
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Esempio di richiesta autenticata verso NeonDB (tramite server)
         */
        public String richiediDatiProtetti() {
            if (jwtToken == null) {
                return "Errore: Non sei loggato!";
            }

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/api/dati"))
                        .header("Authorization", "Bearer " + jwtToken) // Header con il token
                        .header("Content-Type", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    // Qui stiamo ritornando la stringa grezza, ma potresti usare
                    // objectMapper.readValue(...) per convertirla in una List<User> o altro.
                    return response.body();
                } else {
                    return "Errore Server: " + response.statusCode();
                }

            } catch (Exception e) {
                return "Eccezione: " + e.getMessage();
            }
        }

        // DTO per ricevere la risposta con il Token
        public static class AuthResponse {
            private String token;

            // Costruttore vuoto (obbligatorio)
            public AuthResponse() {}

            // Getter e Setter (necessari per il mapping)
            public String getToken() { return token; }
            public void setToken(String token) { this.token = token; }
        }

}