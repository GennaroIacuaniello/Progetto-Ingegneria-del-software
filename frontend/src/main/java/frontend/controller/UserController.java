package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import frontend.config.ApiPaths;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller singleton per la gestione delle operazioni sugli utenti lato client.
 * <p>
 * Si occupa principalmente della ricerca di utenti con ruolo Sviluppatore o Amministratore
 * in diversi contesti (globale, all'interno di un progetto, all'interno di un team).
 * Mantiene una cache locale ({@code users}) con i risultati dell'ultima ricerca effettuata,
 * utile per popolare liste e menu a tendina nell'interfaccia grafica.
 * </p>
 */
@SuppressWarnings("java:S6548")
public class UserController {

    /**
     * Istanza unica della classe (Pattern Singleton).
     */
    private static UserController instance;

    /**
     * Riferimento al client HTTP per le comunicazioni di rete.
     */
    private final ApiClient client = ApiClient.getInstance();

    /**
     * Logger per la registrazione degli eventi.
     */
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    /**
     * Cache locale degli utenti trovati nell'ultima ricerca effettuata.
     */
    private ArrayList<UserDTO> users;

    /**
     * Costante per il path HTTP.
     */
    private static final String USERS_PATH = ApiPaths.USERS;

    /**
     * Costante per il path HTTP.
     */
    private static final String DEVELOPER_SEARCH_PATH = "developers/search?email=";

    /**
     * Costruttore privato per il Singleton.
     */
    private UserController(){

    }

    /**
     * Restituisce l'istanza unica di UserController.
     *
     * @return L'istanza singleton.
     */
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    /**
     * Cerca sviluppatori o amministratori filtrando per email e per il progetto corrente.
     * <p>
     * Utilizzato per trovare utenti che lavorano già al progetto selezionato in {@link ProjectController}.
     * </p>
     *
     * @param devEmail L'email (o parte di essa) da cercare.
     * @return {@code true} se la ricerca va a buon fine (200 o 204).
     */
    public boolean searchDevOrAdminByEmailAndProject(String devEmail) {

        //Encoder to use since there cannot be spaces in search parameters of HTTP requests
        String encodedDevEmail = URLEncoder.encode(devEmail, StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(client.getBaseUrl() + USERS_PATH + DEVELOPER_SEARCH_PATH + encodedDevEmail +
                                "&projectId=" + ProjectController.getInstance().getProject().getId()))
                .GET();

        return handleHttpRequest(requestBuilder);

    }

    /**
     * Cerca sviluppatori o amministratori filtrando per email e per il team corrente.
     * <p>
     * Utilizzato per trovare membri che appartengono già al team selezionato in {@link TeamController}.
     * </p>
     *
     * @param devEmail L'email (o parte di essa) da cercare.
     * @return {@code true} se la ricerca va a buon fine.
     */
    public boolean searchDevOrAdminByEmailAndTeam(String devEmail) {

        //Encoder to use since there cannot be spaces in search parameters of HTTP requests
        String encodedDevEmail = URLEncoder.encode(devEmail, StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(client.getBaseUrl() + USERS_PATH + DEVELOPER_SEARCH_PATH + encodedDevEmail +
                        "&teamId=" + TeamController.getInstance().getTeam().getId() ))
                .GET();

        return handleHttpRequest(requestBuilder);

    }

    /**
     * Cerca sviluppatori o amministratori globalmente nel sistema tramite email.
     * <p>
     * Utilizzato, ad esempio, quando si vuole aggiungere un nuovo membro a un progetto
     * cercandolo tra tutti gli utenti registrati.
     * </p>
     *
     * @param devEmail L'email (o parte di essa) da cercare.
     * @return {@code true} se la ricerca va a buon fine.
     */
    public boolean searchDevOrAdminByEmail(String devEmail) {


        //Encoder to use since there cannot be spaces in search parameters of HTTP requests
        String encodedDevEmail = URLEncoder.encode(devEmail, StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(client.getBaseUrl() + USERS_PATH + DEVELOPER_SEARCH_PATH + encodedDevEmail))
                .GET();

        return handleHttpRequest(requestBuilder);

    }

    /**
     * Metodo helper privato per gestire l'invio della richiesta e il parsing della risposta.
     * <p>
     * Gestisce i codici di stato:
     * <ul>
     * <li>**200 OK**: Popola la lista {@code users} con i risultati deserializzati.</li>
     * <li>**204 No Content**: Pulisce la lista {@code users} (nessun risultato).</li>
     * <li>**Errore**: Logga l'errore e pulisce la lista.</li>
     * </ul>
     * </p>
     *
     * @param requestBuilder Il builder della richiesta HTTP preconfigurato.
     * @return {@code true} se l'operazione è considerata un successo (anche se non ci sono risultati).
     */
    private boolean handleHttpRequest(HttpRequest.Builder requestBuilder){

        try {

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.users = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});
                logger.log(Level.FINE, "Search completed successfully. Number of users founded: {0}", this.users.size());

                return true;

            } else if (response.statusCode() == 204) {

                //No results, clear the cache
                this.users = new ArrayList<>();

                logger.log(Level.FINE, "Search completed successfully, BUT no users were found. ");

            } else {

                // Generic error

                String errorMsg = client.getErrorMessageFromResponse(response);

                logger.log(Level.WARNING, "Users search failed. Error: {0}", errorMsg);

                this.users = new ArrayList<>();

            }

        }  catch (RequestError re) {

            logger.log(Level.WARNING, "Backend offline: {0}", re.getMessage());
            this.users = new ArrayList<>();

        } catch (Exception e) {

            logger.log(Level.SEVERE, e.getMessage());
            this.users = new ArrayList<>();
        }

        return false;

    }

    /**
     * Restituisce una lista contenente solo le email degli utenti trovati nell'ultima ricerca.
     * <p>
     * Utile per popolare componenti grafici come JComboBox o liste di suggerimenti.
     * </p>
     *
     * @return Lista di stringhe (email).
     */
    public List<String> getUsersEmails(){

        ArrayList<String> emails = new ArrayList<>();

        for(UserDTO u: users)
            emails.add(u.getEmail());

        return emails;

    }

}
