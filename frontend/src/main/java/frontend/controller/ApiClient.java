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

/**
 * Client HTTP singleton per la gestione centralizzata delle comunicazioni con il backend.
 * <p>
 * Questa classe gestisce l'invio di tutte le richieste HTTP, l'inclusione automatica del token JWT,
 * la gestione degli errori di rete (inclusi timeout e disconnessioni) e il logout forzato
 * in caso di sessione scaduta (401/403).
 * Utilizza {@link HttpClient} per le chiamate di rete e {@link ObjectMapper} per la gestione del JSON.
 * </p>
 */
@SuppressWarnings("java:S6548")
public class ApiClient {

    /**
     * Istanza unica della classe (Singleton).
     */
    private static ApiClient instance;

    /**
     * Logger per la registrazione di errori e warning.
     */
    private static final Logger logger = Logger.getLogger(ApiClient.class.getName());

    /**
     * Client HTTP nativo di Java configurato con timeout.
     */
    private final HttpClient httpClient;

    /**
     * Mapper Jackson per la serializzazione/deserializzazione JSON.
     * <p>
     * È esposto tramite getter (generato da Lombok) per essere utilizzato
     * anche da altre classi del frontend per il parsing dei dati.
     * </p>
     */
    @Getter
    private final ObjectMapper objectMapper;

    /**
     * Token JWT per l'autenticazione.
     * <p>
     * Può essere impostato tramite il setter (generato da Lombok) dopo il login
     * e viene resettato a null in caso di logout o errore critico.
     * </p>
     */
    @Setter
    private String jwtToken = null;

    /**
     * Costruttore privato.
     * <p>
     * Inizializza l'{@code HttpClient} con un timeout di connessione di 10 secondi
     * e configura l'{@code ObjectMapper} registrando il modulo per gestire le date di Java 8 (JavaTimeModule).
     * </p>
     */
    private ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    /**
     * Restituisce l'istanza unica di ApiClient.
     *
     * @return L'istanza singleton.
     */
    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    /**
     * Restituisce l'URL base del backend.
     *
     * @return La stringa dell'URL base.
     */
    public String getBaseUrl() {
        return "http://localhost:8080";
    }

    /**
     * Invia una richiesta HTTP al backend gestendo automaticamente errori e autenticazione.
     * <p>
     * Esegue i seguenti passaggi:
     * </p>
     * <ol>
     * <li>Aggiunge l'header "Authorization" se il token è presente.</li>
     * <li>Invia la richiesta e attende la risposta.</li>
     * <li>Se riceve un errore 401 o 403 (e non è una richiesta di login), forza il logout dell'utente.</li>
     * <li>Gestisce eccezioni di I/O (server non raggiungibile) mostrando un errore all'utente.</li>
     * </ol>
     *
     * @param requestBuilder Il builder della richiesta HTTP pre-configurato (URL, metodo, body).
     * @return La risposta HTTP ricevuta.
     * @throws RequestError Se si verifica un errore di rete o la richiesta viene interrotta.
     */
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

    /**
     * Gestisce errori critici che richiedono il ritorno alla schermata di login.
     * <p>
     * Esegue le seguenti operazioni nel thread dell'interfaccia grafica (EDT):
     * <ul>
     * <li>Resetta il token JWT.</li>
     * <li>Chiude tutte le finestre aperte.</li>
     * <li>Mostra un dialog di errore all'utente.</li>
     * <li>Apre una nuova finestra di login.</li>
     * </ul>
     * </p>
     *
     * @param message Il messaggio da mostrare all'utente nel dialog di errore.
     */
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

    /**
     * Estrae un messaggio di errore leggibile dal corpo JSON della risposta.
     * <p>
     * Tenta di parsare il body come JSON e cercare il campo "message".
     * Se il parsing fallisce, restituisce una stringa generica con il codice di stato e il body grezzo.
     * </p>
     *
     * @param response La risposta HTTP contenente l'errore.
     * @return Il messaggio di errore estratto.
     */
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