package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import frontend.config.ApiPaths;
import frontend.dto.IssueDTO;
import frontend.dto.IssueStatusDTO;
import frontend.dto.ProjectDTO;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller singleton per la gestione delle operazioni sulle segnalazioni (Issue) lato client.
 * <p>
 * Interagisce con il backend per creare, cercare, aggiornare e recuperare i dettagli delle issue.
 * Mantiene lo stato locale delle issue cercate ({@code issues}) e della issue correntemente selezionata ({@code issue}),
 * fungendo da ponte tra la logica di business e l'interfaccia grafica.
 * </p>
 */
@SuppressWarnings("java:S6548")
public class IssueController {

    /**
     * Istanza unica della classe (Pattern Singleton).
     */
    private static IssueController instance;

    /**
     * Riferimento al client HTTP per le comunicazioni di rete.
     */
    private final ApiClient client = ApiClient.getInstance();

    /**
     * Logger per la registrazione degli eventi.
     */
    private static final Logger logger = Logger.getLogger(IssueController.class.getName());

    /**
     * Lista delle issue recuperate dall'ultima ricerca effettuata.
     */
    private ArrayList<IssueDTO> issues;

    /**
     * La issue attualmente selezionata o in visualizzazione.
     */
    @Setter
    private IssueDTO issue;

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
    private static final String ISSUES_PATH = ApiPaths.ISSUES;

    /**
     * Costante per i parametri di ricerca.
     */
    private static final String RESOLVER_ID = "resolverId=";

    /**
     * Costante per i parametri di ricerca.
     */
    private static final String REPORTER_ID = "reporterId=";

    /**
     * Costante per i parametri di ricerca.
     */
    private static final String PROJECT_ID = "projectId=";

    /**
     * Costruttore privato per il Singleton.
     */
    private IssueController(){

    }

    /**
     * Restituisce l'istanza unica di IssueController.
     *
     * @return L'istanza singleton.
     */
    public static IssueController getInstance() {
        if (instance == null) {
            instance = new IssueController();
        }
        return instance;
    }

    /**
     * Invia una nuova issue al backend.
     * <p>
     * Completa il DTO con i dati mancanti (utente corrente, progetto corrente, data odierna),
     * legge l'eventuale file immagine convertendolo in byte array e invia la richiesta POST.
     * </p>
     *
     * @param issueToReport Il DTO con i dati di base della issue (titolo, descrizione, ecc.).
     * @param tags          Lista di tag opzionali.
     * @param image         File immagine allegato (opzionale).
     * @return {@code true} se la creazione ha successo, {@code false} altrimenti.
     */
    public boolean reportIssue(IssueDTO issueToReport, List<String> tags, File image) {

        try {

            issueToReport.setStatus(IssueStatusDTO.TODO);


            if (tags != null && !tags.isEmpty()) {
                issueToReport.setTags(String.join(";", tags));
            }

            issueToReport.setReportDate(Date.from(Instant.now()));


            UserDTO reportingUser = AuthController.getInstance().getLoggedUser();

            UserDTO userToSend = new UserDTO();

            userToSend.setId(reportingUser.getId());
            userToSend.setEmail(reportingUser.getEmail());

            issueToReport.setReportingUser(userToSend);


            ProjectDTO relatedProject = ProjectController.getInstance().getProject();

            ProjectDTO projectToSend = new ProjectDTO();
            projectToSend.setId(relatedProject.getId());
            projectToSend.setName(relatedProject.getName());

            issueToReport.setRelatedProject(projectToSend);

            if (image != null) {
                byte[] fileContent = Files.readAllBytes(image.toPath());
                issueToReport.setImage(fileContent);
            }


            String jsonBody = client.getObjectMapper().writeValueAsString(issueToReport);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/issues"))
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                logger.log(Level.FINE, "Issue reported successfully!");

                return true;

            } else {

                logger.log(Level.WARNING, "Issue report failed. Code: {0}", response.statusCode());

                logger.log(Level.WARNING, "Error body: {0}", response.body());

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    /**
     * Metodo helper privato per eseguire ricerche filtrate.
     *
     * @param issueTitle    Filtro per titolo.
     * @param issueStatus   Filtro per stato.
     * @param issueTags     Filtro per tag.
     * @param issueType     Filtro per tipo.
     * @param issuePriority Filtro per priorità.
     * @param roleToSearch  Parametro aggiuntivo per filtrare per ruolo (es. resolverId= o reporterId=).
     * @return {@code true} se la ricerca ha successo, {@code false} altrimenti.
     */
    private boolean searchIssueGeneral(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority, String roleToSearch) {

        List<String> params = setUpSearchParams(issueTitle, issueStatus, issueTags, issueType, issuePriority);

        params.add(roleToSearch + AuthController.getInstance().getLoggedUser().getId());
        params.add(PROJECT_ID + ProjectController.getInstance().getProject().getId());

        String queryString = String.join("&", params);

        HttpResponse<String> response = sendSearchRequest(queryString);

        return handleSearchResponse(response);

    }

    /**
     * Cerca le issue assegnate all'utente corrente (Developer).
     *
     * @param issueTitle    Filtro titolo.
     * @param issueStatus   Filtro stato.
     * @param issueTags     Filtro tag.
     * @param issueType     Filtro tipo.
     * @param issuePriority Filtro priorità.
     * @return {@code true} se la ricerca va a buon fine.
     */
    public boolean searchAssignedIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        return searchIssueGeneral(issueTitle, issueStatus, issueTags, issueType, issuePriority, RESOLVER_ID);

    }

    /**
     * Cerca tutte le issue del progetto corrente (globale).
     *
     * @param issueTitle    Filtro titolo.
     * @param issueStatus   Filtro stato.
     * @param issueTags     Filtro tag.
     * @param issueType     Filtro tipo.
     * @param issuePriority Filtro priorità.
     * @return {@code true} se la ricerca va a buon fine.
     */
    public boolean searchAllIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        List<String> params = setUpSearchParams(issueTitle, issueStatus, issueTags, issueType, issuePriority);

        params.add(PROJECT_ID + ProjectController.getInstance().getProject().getId());

        String queryString = String.join("&", params);

        HttpResponse<String> response = sendSearchRequest(queryString);

        return handleSearchResponse(response);

    }

    /**
     * Cerca le issue segnalate dall'utente corrente (Reporter).
     *
     * @param issueTitle    Filtro titolo.
     * @param issueStatus   Filtro stato.
     * @param issueTags     Filtro tag.
     * @param issueType     Filtro tipo.
     * @param issuePriority Filtro priorità.
     * @return {@code true} se la ricerca va a buon fine.
     */
    public boolean searchReportedIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        return searchIssueGeneral(issueTitle, issueStatus, issueTags, issueType, issuePriority, REPORTER_ID);

    }

    /**
     * Costruisce la lista dei parametri per la query string, codificandoli in URL-safe format.
     */
    private List<String> setUpSearchParams(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority){

        List<String> params = new ArrayList<>();

        if (issueTitle != null && !issueTitle.isEmpty()) {
            //Encoder to use since there cannot be spaces in search parameters of HTTP requests
            params.add("title=" + URLEncoder.encode(issueTitle, StandardCharsets.UTF_8));
        }
        if (issueStatus != null && !issueStatus.isEmpty()) {
            params.add("status=" + URLEncoder.encode(issueStatus, StandardCharsets.UTF_8));
        }
        if (issueTags != null && !issueTags.isEmpty()) {
            params.add("tags=" + URLEncoder.encode(String.join(";", issueTags), StandardCharsets.UTF_8));
        }
        if (issueType != null && !issueType.isEmpty()) {
            params.add("type=" + URLEncoder.encode(issueType, StandardCharsets.UTF_8));
        }
        if (issuePriority != null && !issuePriority.isEmpty()) {
            params.add("priority=" + priorityStringToInt(issuePriority));
        }
        return params;
    }

    /**
     * Esegue effettivamente la richiesta GET di ricerca al backend.
     */
    private HttpResponse<String> sendSearchRequest(String queryString){

        String fullUrl = client.getBaseUrl() + "/issues/search";

        fullUrl += "?" + queryString;

        logger.log(Level.FINE, "Calling URL: {0}", fullUrl);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .GET();

        return client.sendRequest(requestBuilder);

    }

    /**
     * Gestisce la risposta del backend alla ricerca, popolando la lista {@code issues}.
     *
     * @param response La risposta HTTP.
     * @return {@code true} se sono stati trovati risultati o la lista è vuota ma la richiesta è valida (200/204).
     */
    private boolean handleSearchResponse(HttpResponse<String> response){

        try{

            if (response.statusCode() == 200) {

                this.issues = client.getObjectMapper().readValue(response.body(), new TypeReference<>(){});
                logger.log(Level.FINE, "Search completed successfully. Number of issues founded: {0}", this.issues.size());
                return true;

            } else if (response.statusCode() == 204) {

                this.issues = new ArrayList<>();
                logger.log(Level.FINE, "Search completed successfully, BUT no issues were found. ");
                return true;

            } else {
                // Generic error

                String errorMsg = client.getErrorMessageFromResponse(response);

                logger.log(Level.WARNING, "Issue search failed. Error: {0}", errorMsg);

                this.issues = new ArrayList<>();

            }

        } catch (RequestError re) {

            logger.log(Level.WARNING, "Backend offline: {0}", re.getMessage());
            this.issues = new ArrayList<>();

        } catch (Exception e) {

            logger.log(Level.SEVERE, e.getMessage());
            this.issues = new ArrayList<>();
        }

        return false;

    }

    /**
     * Recupera i dettagli completi della issue attualmente selezionata ({@code issue}) tramite il suo ID.
     * <p>
     * Utile per aggiornare le informazioni prima di visualizzarle, assicurandosi di avere i dati più recenti.
     * </p>
     *
     * @return {@code true} se il recupero ha successo.
     */
    public boolean getIssueById() {

        try {

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + ISSUES_PATH + issue.getId()))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.issue = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});
                logger.log(Level.FINE, "Search of issue by ID completed successfully. Issue id: {0}", this.issue.getId());
                return true;

            } else if (response.statusCode() == 404) {

                logger.log(Level.WARNING, "Issue search failed. Searched ID was {0}", issue.getId());
                this.issue = null;

            } else {

                String errorMsg = client.getErrorMessageFromResponse(response);
                logger.log(Level.WARNING, "Server error: {0}", errorMsg);
                this.issue = null;

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            this.issue = null;
        }

        return false;

    }

    /**
     * Imposta lo stato della issue corrente a "RESOLVED".
     *
     * @return {@code true} se l'aggiornamento ha successo.
     */
    public boolean setIssueAsResolved() {

        try {

            StatusUpdateRequest requestBody = new StatusUpdateRequest(IssueStatusDTO.RESOLVED);

            String jsonBody = client.getObjectMapper().writeValueAsString(requestBody);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + ISSUES_PATH + issue.getId() + "/status"))
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                issue.setStatus(IssueStatusDTO.RESOLVED);
                logger.log(Level.FINE, "Issue status update success: {0}", response.body());

                return true;
            } else if (response.statusCode() == 404) {

                logger.log(Level.WARNING, "Issue status update failed because the issue was not found. Searched ID was {0}", issue.getId());
                this.issue = null;

            }else {

                String errorMsg = client.getErrorMessageFromResponse(response);
                logger.log(Level.WARNING, "Error in status update: {0}", errorMsg);

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

    /**
     * Assegna la issue corrente a uno sviluppatore specificato tramite email.
     *
     * @param resolverEmail L'email dello sviluppatore a cui assegnare la issue.
     * @return {@code true} se l'assegnazione ha successo.
     */
    public boolean assignIssueToDeveloper(String resolverEmail) {

        try {

            UserDTO resolver = new UserDTO();
            resolver.setEmail(resolverEmail);

            String jsonBody = client.getObjectMapper().writeValueAsString(resolver);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + ISSUES_PATH + issue.getId() + "/resolver"))
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                UserDTO fullResolverInfo = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});

                issue.setAssignedDeveloper(fullResolverInfo);

                logger.log(Level.FINE, "Issue assigned correctly to: {0}", fullResolverInfo.getEmail());

                return true;

            } else if (response.statusCode() == 404) {

                logger.log(Level.WARNING, "Issue assigning failed because either the issue or the user were not found. Searched ID was {0}, user email was {1}", new Object[]{issue.getId(), resolverEmail});

            } else {

                String errorMsg = client.getErrorMessageFromResponse(response);
                logger.log(Level.WARNING, "Error in issue assigment: {0}", errorMsg);

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

    /**
     * DTO interno statico per la richiesta di aggiornamento stato.
     */
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {

        private IssueStatusDTO newStatus;

    }

    /**
     * Restituisce una lista con i titoli di tutte le issue correntemente caricate.
     */
    public List<String> getIssuesTitles () {

        List<String> issuesTitles = new ArrayList<>();

        for (IssueDTO i : issues)
            issuesTitles.add(i.getTitle());

        return issuesTitles;
    }

    /**
     * Restituisce il titolo della issue attualmente selezionata.
     */
    public String getIssueTitle() {
        return issue.getTitle();
    }

    /**
     * Restituisce la descrizione della issue attualmente selezionata.
     */
    public String getIssueDescription() {
        return issue.getDescription();
    }

    /**
     * Restituisce lo stato della issue attualmente selezionata.
     */
    public String getIssueStatus() {
        return issue.getStatus().toString();
    }

    /**
     * Restituisce la priorità della issue corrente convertita in stringa leggibile (es. "Molto Alta").
     */
    public String getIssuePriority() {

        return priorityIntToString(issue.getPriority());
    }

    /**
     * Restituisce il tipo della issue attualmente selezionata.
     */
    public String getIssueType() {
        return issue.getType().toString();
    }

    /**
     * Converte l'immagine della issue (byte array) in un file temporaneo.
     * <p>
     * Utile per visualizzare l'immagine o aprirla con il visualizzatore di sistema.
     * Il file temporaneo viene marcato per l'eliminazione all'uscita.
     * </p>
     *
     * @return Il file temporaneo dell'immagine, o {@code null} in caso di errore.
     */
    public File getIssueImageAsFile() {

        try {
            File tempFile = File.createTempFile("issue_img_", ".jpg");


            Files.write(tempFile.toPath(), issue.getImage());

            tempFile.deleteOnExit();

            return tempFile;
        }catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    /**
     * Restituisce i tag della issue corrente come lista di stringhe.
     */
    public List<String> getIssueTagsAsList () {

        String tagsString = issue.getTags();

        if (tagsString == null)
            tagsString = "";

        String[] tagsArray = tagsString.split(";");

        return Arrays.asList(tagsArray);

    }

    /**
     * Restituisce la data di risoluzione della issue attualmente selezionata.
     */
    public Date getIssueResolutionDate () {
        return issue.getResolutionDate();
    }

    /**
     * Restituisce la data di segnalazione della issue attualmente selezionata.
     */
    public Date getIssueReportDate () {
        return issue.getReportDate();
    }

    /**
     * Restituisce l'utente che ha segnalato la issue attualmente selezionata.
     */
    public UserDTO getIssueReportingUser () {
        return issue.getReportingUser();
    }

    /**
     * Restituisce l'utente a cui è assegnata la issue attualmente selezionata.
     */
    public UserDTO getIssueAssignedDeveloper () {
        return issue.getAssignedDeveloper();
    }

    /**
     * Recupera una issue dalla lista dei risultati in base all'indice.
     *
     * @param index Indice della issue nella lista {@code issues}.
     * @return L'oggetto {@code IssueDTO}.
     */
    public IssueDTO getIssueFromIndex(int index) {

        return issues.get(index);
    }

    /**
     * Converte il valore numerico della priorità in una stringa descrittiva in italiano.
     *
     * @param priority Il livello di priorità (0-4).
     * @return La descrizione testuale (es. "Media").
     */
    public String priorityIntToString(int priority) {

        return switch (priority) {
            case 0 -> "Molto Bassa";
            case 1 -> "Bassa";
            case 2 -> "Media";
            case 3 -> "Alta";
            case 4 -> "Molto Alta";
            default -> "Errore";
        };
    }

    /**
     * Converte la descrizione testuale della priorità nel corrispondente valore intero.
     *
     * @param priority La stringa della priorità (es. "Alta").
     * @return Il valore intero (0-4) o -1 se non valido.
     */
    public int priorityStringToInt(String priority) {

        return switch (priority) {
            case "Molto bassa" -> 0;
            case "Bassa" -> 1;
            case "Media" -> 2;
            case "Alta" -> 3;
            case "Molto alta" -> 4;
            default -> -1;
        };
    }

    /**
     * Ribalta l'ArrayList issues per mostrarle all'utente ordinate per priorità crescente o decrescente.
     */
    public void reOrderIssues(){

        IssueDTO issueTmp;
        int size = issues.size();

        for(int i=0; i < size/2; i++){
            issueTmp = issues.get(i);
            issues.set(i, issues.get( size - i -1) );
            issues.set( size - i -1, issueTmp);
        }

    }
}
