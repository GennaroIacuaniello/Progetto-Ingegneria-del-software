package frontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import frontend.exception.RequestError;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {

    private static ApiClient instance;

    private final String BASE_URL = "http://localhost:8080";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String jwtToken = null;

    private ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public void setJwtToken(String token) {
        this.jwtToken = token;
    }

    public HttpResponse<String> sendRequest(HttpRequest.Builder requestBuilder) {

        try {
            if (jwtToken != null) {
                requestBuilder.header("Authorization", "Bearer " + jwtToken);
            }

            HttpRequest request = requestBuilder.build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        }catch (IOException e) {

            System.err.println("Errore di rete: " + e.getMessage());

            throw new RequestError("Impossibile contattare il server. Verifica che il backend sia attivo.");

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new RequestError("Richiesta interrotta dal sistema.");
        }
    }

}