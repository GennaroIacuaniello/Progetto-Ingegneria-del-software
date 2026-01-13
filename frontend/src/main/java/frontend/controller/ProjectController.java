package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import frontend.config.ApiPaths;
import frontend.dto.ProjectDTO;
import frontend.dto.StatisticDTO;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;
import lombok.Getter;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller singleton per la gestione delle operazioni sui progetti lato client.
 * <p>
 * Si occupa di:
 * </p>
 * <ul>
 * <li>Cercare progetti esistenti nel backend.</li>
 * <li>Creare nuovi progetti.</li>
 * <li>Mantenere il riferimento al progetto attualmente selezionato ({@code project}).</li>
 * <li>Recuperare e fornire i dati statistici per la Dashboard ({@code dashboardData}).</li>
 * </ul>
 */
@SuppressWarnings("java:S6548")
public class ProjectController {

    /**
     * Istanza unica della classe (Pattern Singleton).
     */
    private static ProjectController instance;

    /**
     * Riferimento al client HTTP per le comunicazioni di rete.
     */
    private final ApiClient client = ApiClient.getInstance();

    /**
     * Logger per la registrazione degli eventi.
     */
    private static final Logger logger = Logger.getLogger(ProjectController.class.getName());

    /**
     * Cache locale dei progetti trovati nell'ultima ricerca.
     */
    private ArrayList<ProjectDTO> projects;

    /**
     * Il progetto attualmente selezionato dall'utente.
     * <p>
     * Questo campo determina il contesto per altre operazioni (es ricerca issue, gestione team).
     * Il getter è generato automaticamente da Lombok.
     * </p>
     */
    @Getter
    private ProjectDTO project;

    /**
     * DTO contenente le statistiche globali per la visualizzazione della dashboard.
     */
    private StatisticDTO dashboardData;

    /**
     * Costante per le intestazioni HTTP.
     */
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * Costante per le intestazioni HTTP.
     */
    private static final String APPLICATION_JSON = "application/json";

    /**
     * Costante per il path HTTP.
     */
    private static final String PROJECTS_PATH = ApiPaths.PROJECTS;

    /**
     * Costruttore privato per il Singleton.
     */
    private ProjectController(){

    }

    /**
     * Restituisce l'istanza unica di ProjectController.
     *
     * @return L'istanza singleton.
     */
    public static ProjectController getInstance() {
        if (instance == null) {
            instance = new ProjectController();
        }
        return instance;
    }

    /**
     * Cerca i progetti nel backend filtrando per nome.
     * <p>
     * Codifica il parametro di ricerca per l'URL e invia una richiesta GET.
     * Gestisce tre casi:
     * </p>
     * <ul>
     * <li>**200 OK**: Popola la lista {@code projects} con i risultati.</li>
     * <li>**204 No Content**: Svuota la lista (nessun progetto trovato).</li>
     * <li>**Errore**: Logga l'errore e svuota la lista.</li>
     * </ul>
     *
     * @param projectName Il nome (o parte di esso) del progetto da cercare.
     * @return {@code true} se l'operazione (ricerca o nessun risultato) va a buon fine senza errori di rete.
     */
    public boolean searchProjectsByName(String projectName) {

        try {

            //Encoder to use since there cannot be spaces in search parameters of HTTP requests
            String encodedProjectName = URLEncoder.encode(projectName, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + PROJECTS_PATH +"search?name=" + encodedProjectName))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.projects = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});
                logger.log(Level.FINE, "Search completed successfully. Number of projects founded: {0}", this.projects.size());

                return true;

            } else if (response.statusCode() == 204) {

                //No results, clear the cache
                this.projects = new ArrayList<>();
                logger.log(Level.FINE, "Search completed successfully, BUT no projects were found. ");

                return true;

            } else {

                // Generic error

                String errorMsg = client.getErrorMessageFromResponse(response);

                logger.log(Level.WARNING, "Projects search failed. Error: {0}", errorMsg);

                this.projects = new ArrayList<>();

            }

        } catch (RequestError re) {

            logger.log(Level.WARNING, "Backend offline: {0}", re.getMessage());
            this.projects = new ArrayList<>();

        } catch (Exception e) {

            logger.log(Level.SEVERE, e.getMessage());
            this.projects = new ArrayList<>();
        }

        return false;

    }

    /**
     * Invia una richiesta per creare un nuovo progetto.
     *
     * @param projectName Il nome del nuovo progetto.
     * @return {@code true} se il progetto viene creato con successo (HTTP 200).
     */
    public boolean createProject(String projectName){

        try {

           ProjectDTO projectToCreate = new ProjectDTO();
           projectToCreate.setName(projectName);

            String jsonBody = client.getObjectMapper().writeValueAsString(projectToCreate);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/projects"))
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                logger.log(Level.FINE, "Project created successfully!");

                return true;

            } else {

                logger.log(Level.WARNING, "Project creation failed. Code: {0}", response.statusCode());

                logger.log(Level.WARNING, "Error body: {0}", response.body());

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

    /**
     * Recupera i dati statistici per popolare la Dashboard.
     * <p>
     * Effettua una chiamata GET all'endpoint dashboard e deserializza la risposta
     * nell'oggetto {@code StatisticDTO} memorizzato in {@code dashboardData}.
     * </p>
     *
     * @return {@code true} se i dati vengono recuperati correttamente.
     */
    public boolean createDashBoard() {

        try {

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + PROJECTS_PATH + "dashboard"))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.dashboardData = client.getObjectMapper().readValue(response.body(), StatisticDTO.class);
                logger.log(Level.FINE, "Dashboard generated successfully!");

                return true;

            } else {

                logger.log(Level.WARNING, "Error dashboard generation. Code: {0}", response.statusCode());

                logger.log(Level.WARNING, "Error body: {0}", response.body());

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

    /**
     * Restituisce una lista degli ID dei progetti trovati nell'ultima ricerca.
     *
     * @return Una lista di {@code Integer} contenente gli ID dei progetti.
     */
    public List<Integer> getProjectsIds () {

        List<Integer> ids = new ArrayList<>();

        for (ProjectDTO p : projects)
            ids.add(p.getId());

        return ids;
    }

    /**
     * Restituisce una lista dei nomi dei progetti trovati nell'ultima ricerca.
     *
     * @return Una lista di {@code String} con i nomi dei progetti.
     */
    public List<String> getProjectsNames () {

        List<String> names = new ArrayList<>();

        for (ProjectDTO p : projects)
            names.add(p.getName());

        return names;
    }

    /**
     * Imposta manualmente il progetto corrente utilizzando ID e nome forniti.
     *
     * @param id   L'ID del progetto.
     * @param name Il nome del progetto.
     */
    public void setProjectWithValues(int id, String name) {

        this.project = new ProjectDTO(id, name);
    }

    /**
     * Imposta il progetto corrente cercandolo per ID nella lista dei progetti caricati ({@code projects}).
     *
     * @param id L'ID del progetto da selezionare.
     */
    public void setProjectWithId(int id) {

        for(ProjectDTO p: projects)
            if(p.getId() == id){
                this.project = p;
                break;
            }


    }

    /**
     * Restituisce le email degli sviluppatori coinvolti nelle statistiche della dashboard.
     *
     * @return Una lista di stringhe contenente le email degli sviluppatori.
     */
    public List<String> getDevelopersEmails() {

        ArrayList<String> emails = new ArrayList<>();

        for(UserDTO dev : this.dashboardData.getDevelopers())
            emails.add(dev.getEmail());

        return emails;

    }

    /**
     * Restituisce la lista del numero di issue aperte per ogni sviluppatore.
     *
     * @return Una lista di interi rappresentante il conteggio delle issue aperte per dev.
     */
    public List<Integer> getOpenIssues() {
        return this.dashboardData.getNumOpenIssues();
    }

    /**
     * Restituisce la lista del numero di issue risolte per ogni sviluppatore.
     *
     * @return Una lista di interi rappresentante il conteggio delle issue risolte per dev.
     */
    public List<Integer> getResolvedIssues() {
        return this.dashboardData.getNumClosedIssues();
    }

    /**
     * Restituisce la lista delle durate medie di risoluzione per ogni sviluppatore.
     *
     * @return Una lista di oggetti {@code Duration}.
     */
    public List<Duration> getAverageResolvingDurations() {
        return this.dashboardData.getAverageResolutionDurations();
    }

    /**
     * Calcola il numero totale di issue aperte sommando i valori individuali.
     *
     * @return Il numero totale di issue aperte nel sistema.
     */
    public int getTotalOpenIssues() {

        int total = 0;

        for (Integer i : this.dashboardData.getNumOpenIssues())
            total += i;

        return total;
    }

    /**
     * Calcola il numero totale di issue risolte sommando i valori individuali.
     *
     * @return Il numero totale di issue risolte nel sistema.
     */
    public int getTotalResolvedIssues() {

        int total = 0;

        for (Integer i : this.dashboardData.getNumClosedIssues())
            total += i;

        return total;
    }

    /**
     * Restituisce la durata media globale di risoluzione.
     *
     * @return La durata media complessiva come oggetto {@code Duration}.
     */
    public Duration getTotalAverageResolvingDuration() {
        return this.dashboardData.getTotalAverageResolutionDuration();
    }

    /**
     * Restituisce il numero di issue che non sono state ancora assegnate a nessuno sviluppatore.
     *
     * @return Il conteggio delle issue non assegnate.
     */
    public Integer getNumIssuesNotAssigned(){
        return this.dashboardData.getNumIssuesNotAssigned();
    }

}
