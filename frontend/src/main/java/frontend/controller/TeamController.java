package frontend.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import frontend.config.ApiPaths;
import frontend.dto.TeamDTO;
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
 * Controller singleton per la gestione delle operazioni sui team lato client.
 * <p>
 * Questa classe offre metodi per:
 * <ul>
 * <li>Creare nuovi team all'interno del progetto corrente.</li>
 * <li>Cercare i team esistenti.</li>
 * <li>Aggiungere o rimuovere membri dai team.</li>
 * <li>Generare report statistici mensili per un team specifico ({@code teamReport}).</li>
 * </ul>
 * </p>
 */
@SuppressWarnings("java:S6548")
public class TeamController {

    /**
     * Istanza unica della classe (Pattern Singleton).
     */
    private static TeamController instance;

    /**
     * Riferimento al client HTTP per le comunicazioni di rete.
     */
    private final ApiClient client = ApiClient.getInstance();

    /**
     * Logger per la registrazione degli eventi.
     */
    private static final Logger logger = Logger.getLogger(TeamController.class.getName());

    /**
     * Cache locale dei team trovati nell'ultima ricerca.
     */
    private ArrayList<TeamDTO> teams;

    /**
     * Il team attualmente selezionato dall'utente per le operazioni di dettaglio (es report).
     */
    @Getter
    private TeamDTO team;

    /**
     * DTO contenente le statistiche del report generato per il team selezionato.
     */
    private StatisticDTO teamReport;

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
    private static final String TEAMS_PATH = ApiPaths.TEAMS;

    /**
     * Costruttore privato per il Singleton.
     */
    private TeamController(){

    }

    /**
     * Restituisce l'istanza unica di TeamController.
     *
     * @return L'istanza singleton.
     */
    public static TeamController getInstance() {
        if (instance == null) {
            instance = new TeamController();
        }
        return instance;
    }

    /**
     * Crea un nuovo team associato al progetto corrente.
     * <p>
     * Costruisce un {@code TeamDTO} con il nome specificato, il progetto attuale
     * (recuperato da {@link ProjectController}) e l'utente corrente come primo membro.
     * </p>
     *
     * @param teamName Il nome del team da creare.
     * @return {@code true} se la creazione ha successo.
     */
    public boolean createTeam(String teamName){

        try {

            TeamDTO teamToCreate = new TeamDTO();
            teamToCreate.setName(teamName);
            teamToCreate.setProject(ProjectController.getInstance().getProject());
            ArrayList<UserDTO> developers = new ArrayList<>();
            developers.add(AuthController.getInstance().getLoggedUser());
            teamToCreate.setDevelopers(developers);

            String jsonBody = client.getObjectMapper().writeValueAsString(teamToCreate);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/teams"))
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                logger.log(Level.FINE, "Team created successfully!");

                return true;

            } else {

                logger.log(Level.WARNING, "Team creation failed. Code: {0}", response.statusCode());

                logger.log(Level.WARNING, "Error body: {0}", response.body());

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

    /**
     * Cerca i team in base al nome all'interno del progetto corrente.
     *
     * @param teamName Il nome (o parte di esso) del team da cercare.
     * @return {@code true} se la ricerca va a buon fine (200 o 204).
     */
    public boolean searchTeamsByNameAndProject(String teamName) {

        try {

            //Encoder to use since there cannot be spaces in search parameters of HTTP requests
            String encodedTeamName = URLEncoder.encode(teamName, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + TEAMS_PATH + "search?teamName=" + encodedTeamName +
                            "&projectId=" + ProjectController.getInstance().getProject().getId()))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.teams = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});

                logger.log(Level.FINE, "Search completed successfully. Number of teams founded: {0}", this.teams.size());

                return true;

            } else if (response.statusCode() == 204) {

                //No results, clear the cache
                this.teams = new ArrayList<>();

                logger.log(Level.FINE, "Search completed successfully, BUT no teams were found. ");

                return true;

            } else {

                // Generic error

                String errorMsg = client.getErrorMessageFromResponse(response);

                logger.log(Level.WARNING, "Projects search failed. Error: {0}", errorMsg);

                this.teams = new ArrayList<>();

            }

        } catch (RequestError re) {

            logger.log(Level.WARNING, "Backend offline: {0}", re.getMessage());
            this.teams = new ArrayList<>();

        } catch (Exception e) {

            logger.log(Level.SEVERE, e.getMessage());
            this.teams = new ArrayList<>();
        }

        return false;

    }

    /**
     * Rimuove un membro dal team correntemente selezionato.
     *
     * @param emailUserToRemove L'email dell'utente da rimuovere.
     * @return {@code true} se la rimozione ha successo.
     */
    public boolean removeMemberFromSelectedTeam(String emailUserToRemove){

        try {

            //Encoder to use since there cannot be spaces in search parameters of HTTP requests
            String encodedUserEmail = URLEncoder.encode(emailUserToRemove, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + TEAMS_PATH + "remove-member?teamId=" + TeamController.getInstance().getTeam().getId() + "&email=" + encodedUserEmail))
                    .DELETE();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                logger.log(Level.FINE, "Member deleted successfully: {0}", response.body());

                return true;

            }else if (response.statusCode() == 404) {

                logger.log(Level.WARNING, "Member deletion error because either the team or the user were not found, or the user was not in the team. Searched ID was {0}, user email was {1}", new Object[]{team.getId(), emailUserToRemove});

            } else {

                String errorMsg = client.getErrorMessageFromResponse(response);
                logger.log(Level.WARNING, "Member deletion error: {0}", errorMsg);

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

    /**
     * Aggiunge un nuovo membro al team correntemente selezionato.
     *
     * @param emailUserToAdd L'email dell'utente da aggiungere.
     * @return {@code 0} se l'aggiunta ha successo, 1 o 2 come codici di errore altrimenti.
     */
    public Integer addMemberToSelectedTeam(String emailUserToAdd) {

        try {

            //Encoder to use since there cannot be spaces in search parameters of HTTP requests
            String encodedUserEmail = URLEncoder.encode(emailUserToAdd, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + TEAMS_PATH + "add-member?teamId=" + TeamController.getInstance().getTeam().getId() + "&email=" + encodedUserEmail))
                    .POST(HttpRequest.BodyPublishers.noBody());

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                logger.log(Level.FINE, "Member added successfully: {0}", response.body());

                return 0;

            }else if (response.statusCode() == 409) {

                logger.log(Level.WARNING, "Member adding error because either the team or the user were not found, or the user was already in the team. Searched ID was {0}, user email was {1}", new Object[]{team.getId(), emailUserToAdd});

            } else {

                String errorMsg = client.getErrorMessageFromResponse(response);
                logger.log(Level.WARNING, "Member adding error: {0}", errorMsg);
                return 1;

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return 2;

    }

    /**
     * Genera e recupera il report statistico per il team selezionato, filtrato per mese e anno.
     *
     * @param month Il mese del report (stringa).
     * @param year  L'anno del report (stringa).
     * @return {@code true} se il report viene generato correttamente.
     */
    public boolean createReport(String month, String year) {
        try {

            String encodedMonth = URLEncoder.encode(month, StandardCharsets.UTF_8);
            String encodedYear = URLEncoder.encode(year, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + TEAMS_PATH + this.team.getId() + "/report?month=" + encodedMonth + "&year=" + encodedYear))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.teamReport = client.getObjectMapper().readValue(response.body(), StatisticDTO.class);

                logger.log(Level.FINE, "Report generated successfully!");

                return true;

            } else {

                logger.log(Level.WARNING, "Error report generation. Code: {0}", response.statusCode());

                logger.log(Level.WARNING, "Error body: {0}", response.body());

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

    /**
     * Restituisce una lista degli ID dei team trovati nell'ultima ricerca.
     *
     * @return Lista di interi (ID).
     */
    public List<Integer> getTeamsIds () {

        List<Integer> ids = new ArrayList<>();

        for (TeamDTO t : teams)
            ids.add(t.getId());

        return ids;
    }

    /**
     * Restituisce una lista dei nomi dei team trovati nell'ultima ricerca.
     *
     * @return Lista di stringhe (nomi).
     */
    public List<String> getTeamsNames () {

        List<String> names = new ArrayList<>();

        for (TeamDTO t : teams)
            names.add(t.getName());

        return names;
    }

    /**
     * Imposta il team corrente cercandolo per ID nella lista dei team caricati ({@code teams}).
     *
     * @param id L'ID del team da selezionare.
     */
    public void setTeamWithId(int id) {

        for(TeamDTO t: teams)
            if(t.getId() == id){
                this.team = t;
                break;
            }


    }

    /**
     * Restituisce le email degli sviluppatori inclusi nel report.
     *
     * @return Lista di email.
     */
    public List<String> getDevelopersEmails() {

        ArrayList<String> emails = new ArrayList<>();

        for(UserDTO dev : this.teamReport.getDevelopers())
            emails.add(dev.getEmail());

        return emails;

    }

    /**
     * Restituisce il numero di issue aperte per ogni sviluppatore nel periodo del report.
     *
     * @return Lista di conteggi.
     */
    public List<Integer> getOpenIssues() {
        return this.teamReport.getNumOpenIssues();
    }

    /**
     * Restituisce il numero di issue risolte per ogni sviluppatore nel periodo del report.
     *
     * @return Lista di conteggi.
     */
    public List<Integer> getResolvedIssues() {
        return this.teamReport.getNumClosedIssues();
    }

    /**
     * Restituisce le durate medie di risoluzione per ogni sviluppatore nel periodo del report.
     *
     * @return Lista di {@code Duration}.
     */
    public List<Duration> getAverageResolvingDurations() {
        return this.teamReport.getAverageResolutionDurations();
    }

    /**
     * Calcola il numero totale di issue aperte nel report.
     *
     * @return Totale issue aperte.
     */
    public int getTotalOpenIssues() {

        int total = 0;

        for (Integer i : this.teamReport.getNumOpenIssues())
            total += i;

        return total;
    }

    /**
     * Calcola il numero totale di issue risolte nel report.
     *
     * @return Totale issue risolte.
     */
    public int getTotalResolvedIssues() {

        int total = 0;

        for (Integer i : this.teamReport.getNumClosedIssues())
            total += i;

        return total;
    }

    /**
     * Restituisce la durata media globale di risoluzione per il team nel periodo del report.
     *
     * @return Durata media totale.
     */
    public Duration getTotalAverageResolvingDuration() {
        return this.teamReport.getTotalAverageResolutionDuration();
    }

}
