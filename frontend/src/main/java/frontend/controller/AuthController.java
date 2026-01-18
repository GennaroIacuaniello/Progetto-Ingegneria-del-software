package frontend.controller;

import frontend.dto.UserDTO;
import lombok.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller singleton responsabile della gestione dell'autenticazione lato client.
 * <p>
 * Questa classe funge da intermediario tra l'interfaccia grafica e gli endpoint di autenticazione
 * del backend (/auth/login e /auth/register). Gestisce il flusso di login, il salvataggio
 * dell'utente corrente e la registrazione di nuovi utenti.
 * </p>
 */
@SuppressWarnings("java:S6548")
public class AuthController {

    /**
     * Istanza unica della classe (Pattern Singleton).
     */
    private static AuthController instance;

    /**
     * Riferimento al client HTTP per effettuare le chiamate di rete.
     */
    private final ApiClient client = ApiClient.getInstance();

    /**
     * Logger per la registrazione di eventi di login/registrazione ed errori.
     */
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    /**
     * L'oggetto DTO rappresentante l'utente attualmente autenticato nel sistema.
     * <p>
     * Viene popolato dopo un login avvenuto con successo.
     * I metodi getter e setter sono generati automaticamente da Lombok.
     * </p>
     */
    @Setter
    @Getter
    private UserDTO loggedUser;

    /**
     * Costruttore privato per impedire l'istanziazione diretta.
     */
    private AuthController(){

    }

    /**
     * Restituisce l'istanza unica di AuthController.
     *
     * @return L'istanza singleton.
     */
    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }
    
    /**
     * Effettua il tentativo di login al sistema.
     * <p>
     * Prepara una richiesta di login, la serializza in JSON e la invia al backend.
     * Se la risposta è positiva (HTTP 200):
     * </p>
     * <ol>
     * <li>Deserializza la risposta per ottenere il token JWT e i dati utente.</li>
     * <li>Imposta il token nel {@link ApiClient} per le richieste future.</li>
     * <li>Memorizza l'utente loggato in {@code loggedUser}.</li>
     * </ol>
     *
     * @param email    L'indirizzo email dell'utente.
     * @param password La password dell'utente.
     * @return {@code true} se il login ha successo, {@code false} altrimenti.
     */
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

    /**
     * Classe interna statica (DTO) per strutturare la richiesta di login.
     * <p>
     * Utilizza Lombok per generare costruttori, getter, setter, ecc.
     * </p>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {

        private String email;
        private String password;

    }

    /**
     * Classe interna statica (DTO) per strutturare la risposta di autenticazione del backend.
     * <p>
     * Contiene il token JWT generato e i dettagli dell'utente.
     * </p>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {

        private String token;
        private UserDTO user;

    }

    /**
     * Effettua la richiesta di registrazione di un nuovo utente.
     * <p>
     * Invia i dati al backend per creare un nuovo account. Non effettua automaticamente il login dopo la registrazione;
     * l'utente dovrà loggarsi separatamente.
     * </p>
     *
     * @param email    L'email del nuovo utente.
     * @param password La password scelta.
     * @param role     Il ruolo dell'utente (0 per Guest/User, 1 per Developer, 2 per Admin).
     * @return {@code true} se la registrazione ha successo (HTTP 200), {@code false} altrimenti.
     */
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

    /**
     * Classe interna statica (DTO) per strutturare la richiesta di registrazione.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {

        private String email;
        private String password;
        private int role;

    }

}