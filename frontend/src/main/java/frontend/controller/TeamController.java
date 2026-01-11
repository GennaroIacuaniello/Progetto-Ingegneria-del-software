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

@SuppressWarnings("java:S6548")
public class TeamController {

    private static TeamController instance;
    private final ApiClient client = ApiClient.getInstance();

    private static final Logger logger = Logger.getLogger(TeamController.class.getName());


    private ArrayList<TeamDTO> teams;
    @Getter
    private TeamDTO team;

    private StatisticDTO teamReport;

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    private static final String TEAMS_PATH = ApiPaths.TEAMS;


    private TeamController(){

    }

    public static TeamController getInstance() {
        if (instance == null) {
            instance = new TeamController();
        }
        return instance;
    }

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

    public boolean addMemberToSelectedTeam(String emailUserToAdd) {

        try {

            //Encoder to use since there cannot be spaces in search parameters of HTTP requests
            String encodedUserEmail = URLEncoder.encode(emailUserToAdd, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + TEAMS_PATH + "add-member?teamId=" + TeamController.getInstance().getTeam().getId() + "&email=" + encodedUserEmail))
                    .POST(HttpRequest.BodyPublishers.noBody());

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                logger.log(Level.FINE, "Member added successfully: {0}", response.body());

                return true;

            }else if (response.statusCode() == 409) {

                logger.log(Level.WARNING, "Member adding error because either the team or the user were not found, or the user was already in the team. Searched ID was {0}, user email was {1}", new Object[]{team.getId(), emailUserToAdd});

            } else {

                String errorMsg = client.getErrorMessageFromResponse(response);
                logger.log(Level.WARNING, "Member adding error: {0}", errorMsg);

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;

    }

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


    public List<Integer> getTeamsIds () {

        List<Integer> ids = new ArrayList<>();

        for (TeamDTO t : teams)
            ids.add(t.getId());

        return ids;
    }

    public List<String> getTeamsNames () {

        List<String> names = new ArrayList<>();

        for (TeamDTO t : teams)
            names.add(t.getName());

        return names;
    }

    public void setTeamWithId(int id) {

        for(TeamDTO t: teams)
            if(t.getId() == id){
                this.team = t;
                break;
            }


    }

    public List<String> getDevelopersEmails() {

        ArrayList<String> emails = new ArrayList<>();

        for(UserDTO dev : this.teamReport.getDevelopers())
            emails.add(dev.getEmail());

        return emails;

    }

    public List<Integer> getOpenIssues() {
        return this.teamReport.getNumOpenIssues();
    }

    public List<Integer> getResolvedIssues() {
        return this.teamReport.getNumClosedIssues();
    }

    public List<Duration> getAverageResolvingDurations() {
        return this.teamReport.getAverageResolutionDurations();
    }

    public int getTotalOpenIssues() {

        int total = 0;

        for (Integer i : this.teamReport.getNumOpenIssues())
            total += i;

        return total;
    }

    public int getTotalResolvedIssues() {

        int total = 0;

        for (Integer i : this.teamReport.getNumClosedIssues())
            total += i;

        return total;
    }

    public Duration getTotalAverageResolvingDuration() {
        return this.teamReport.getTotalAverageResolutionDuration();
    }

}
