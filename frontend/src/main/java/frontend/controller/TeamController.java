package frontend.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import frontend.dto.TeamDTO;
import frontend.dto.StatisticDTO;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S6548")
public class TeamController {

    private static TeamController instance;
    private final ApiClient client = ApiClient.getInstance();

    private ArrayList<TeamDTO> teams;
    private TeamDTO team;

    private StatisticDTO teamReport;


    private TeamController(){

    }

    public static TeamController getInstance() {
        if (instance == null) {
            instance = new TeamController();
        }
        return instance;
    }

    public void createTeam(String teamName){

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
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {
                System.out.println("Team created successfully!");
            } else {
                System.err.println("Team creation failed. Code: " + response.statusCode());
                System.err.println("Error body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void searchTeamsByNameAndProject(String teamName) {

        try {

            //Da utilizzare dato che non possono esserci spazi nei parametri search delle richieste HTTP
            String encodedTeamName = URLEncoder.encode(teamName, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/teams/search?teamName=" + encodedTeamName +
                            "&projectId=" + ProjectController.getInstance().getProject().getId()))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.teams = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});


            } else if (response.statusCode() == 204) {

                //Nessun risultato, svuoto la cache
                this.teams = new ArrayList<>();

            } else {

                // CASO ERRORE (500, 400, ecc.)
                System.err.println("Errore dal server. Codice: " + response.statusCode());
                System.err.println("Dettaglio errore: " + response.body());
                this.teams = new ArrayList<>();

            }

        } catch (RequestError re) {

            System.err.println("Backend offline: " + re.getMessage());
            this.teams = new ArrayList<>();

        } catch (Exception e) {

            e.printStackTrace();
            this.teams = new ArrayList<>();
        }

    }

    public void removeMemberFromSelectedTeam(String emailUserToRemove){

        try {

            //Da utilizzare dato che non possono esserci spazi nei parametri search delle richieste HTTP
            String encodedUserEmail = URLEncoder.encode(emailUserToRemove, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/teams/remove-member?teamId=" + TeamController.getInstance().getTeam().getId() + "&email=" + encodedUserEmail))
                    .DELETE();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {
                System.out.println("Member deleted successfully!");
            } else {
                System.err.println("Member deletion error. Code: " + response.statusCode());
                System.err.println("Error body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addMemberToSelectedTeam(String emailUserToAdd) {

        try {

            //Da utilizzare dato che non possono esserci spazi nei parametri search delle richieste HTTP
            String encodedUserEmail = URLEncoder.encode(emailUserToAdd, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/teams/add-member?teamId=" + TeamController.getInstance().getTeam().getId() + "&email=" + encodedUserEmail))
                    .POST(HttpRequest.BodyPublishers.noBody());

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {
                System.out.println("Member addedd successfully!");
            } else {
                System.err.println("Member adding error. Code: " + response.statusCode());
                System.err.println("Error body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void createReport(String month, String year) {
        try {

            String encodedMonth = URLEncoder.encode(month, StandardCharsets.UTF_8);
            String encodedYear = URLEncoder.encode(year, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/teams/" + this.team.getId() + "/report?month=" + encodedMonth + "&year=" + encodedYear))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.teamReport = client.getObjectMapper().readValue(response.body(), StatisticDTO.class);

                System.out.println("Report generated successfully!");

            } else {
                System.err.println("Error report generation. Code: " + response.statusCode());
                System.err.println("Error body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public TeamDTO getTeam(){
        return this.team;
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
