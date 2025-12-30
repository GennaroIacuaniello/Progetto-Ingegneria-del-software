package frontend.controller;


import frontend.dto.ProjectDTO;
import frontend.dto.TeamDTO;
import frontend.dto.UserDTO;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class TeamController {

    private static TeamController instance;
    private final ApiClient client = ApiClient.getInstance();

    private ArrayList<TeamDTO> teams;
    private TeamDTO team;


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


}
