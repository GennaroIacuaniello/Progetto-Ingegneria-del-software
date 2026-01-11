package frontend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import frontend.exception.RequestError;
import frontend.gui.LogInPage;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@SuppressWarnings("java:S6548")
public class ApiClient {

    private static ApiClient instance;

    private static final Logger logger = Logger.getLogger(ApiClient.class.getName());

    private final HttpClient httpClient;

    @Getter
    private final ObjectMapper objectMapper;

    @Setter
    private String jwtToken = null;

    private ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public String getBaseUrl() {
        return "http://localhost:8080";
    }

    public HttpResponse<String> sendRequest(HttpRequest.Builder requestBuilder) {

        boolean isLoginRequest = false;
        try {

            if (jwtToken != null) {
                requestBuilder.header("Authorization", "Bearer " + jwtToken);
            }

            HttpRequest request = requestBuilder.build();
            isLoginRequest = request.uri().getPath().endsWith("/login");
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (!isLoginRequest && (response.statusCode() == 401 || response.statusCode() == 403)) {
                handleCriticalError("La sessione è scaduta. \nEffettua nuovamente il login.");
                throw new RequestError();
            }

            return response;


        }catch (IOException e) {

            logger.log(Level.SEVERE, "Errore di connessione al server: {0}", e.toString());

            if(!isLoginRequest)
                handleCriticalError("Impossibile contattare il server.\nRiprovare più tardi.");

            throw new RequestError();

        } catch (InterruptedException e) {

            logger.log(Level.WARNING, "Richiesta interrotta dal sistema", e);

            Thread.currentThread().interrupt();
            throw new RequestError();
        }
    }

    private void handleCriticalError(String message) {

        this.jwtToken = null;

        SwingUtilities.invokeLater(() -> {

            for (Window window : Window.getWindows()) {
                if (window.isDisplayable()) {
                    window.dispose();
                }
            }


            JOptionPane.showMessageDialog(null,
                    message,
                    "Errore di Connessione",
                    JOptionPane.ERROR_MESSAGE);


            new LogInPage().setVisible(true);
        });

    }

    public String getErrorMessageFromResponse(HttpResponse<String> response) {
        try {

            JsonNode node = objectMapper.readTree(response.body());
            if (node.has("message")) {
                return node.get("message").asText();
            }
        } catch (Exception e) {
            logger.log(Level.FINE, "Impossibile fare il parsing del messaggio di errore JSON: {0}", e.getMessage());
        }
        //Basic error message
        return "Errore " + response.statusCode() + ": " + response.body();
    }

}