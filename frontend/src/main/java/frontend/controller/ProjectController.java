package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import frontend.dto.IssueStatusDTO;
import frontend.dto.ProjectDTO;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectController {

    private static ProjectController instance;
    private final ApiClient client = ApiClient.getInstance();

    private ArrayList<ProjectDTO> projects;
    private ProjectDTO project;


    private ProjectController(){

    }

    public static ProjectController getInstance() {
        if (instance == null) {
            instance = new ProjectController();
        }
        return instance;
    }

    public void searchProjectsByName(String projectName) {

        try {

            //Da utilizzare dato che non possono esserci spazi nei parametri search delle richieste HTTP
            String encodedProjectName = URLEncoder.encode(projectName, StandardCharsets.UTF_8);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/projects?search=" + encodedProjectName))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.projects = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});


            } else if (response.statusCode() == 204) {

                //Nessun risultato, svuoto la cache
                this.projects = new ArrayList<>();

            } else {

                // CASO ERRORE (500, 400, ecc.)
                System.err.println("Errore dal server. Codice: " + response.statusCode());
                System.err.println("Dettaglio errore: " + response.body());
                this.projects = new ArrayList<>();

            }

        } catch (RequestError re) {

            System.err.println("Backend offline: " + re.getMessage());
            this.projects = new ArrayList<>();

        } catch (Exception e) {

            e.printStackTrace();
            this.projects = new ArrayList<>();
        }

    }

    public void createProject(String projectName){

        try {

           ProjectDTO projectToCreate = new ProjectDTO();
           projectToCreate.setName(projectName);

            String jsonBody = client.getObjectMapper().writeValueAsString(projectToCreate);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/projects"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {
                System.out.println("Project created successfully!");
            } else {
                System.err.println("Project creation failed. Code: " + response.statusCode());
                System.err.println("Error body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Integer> getProjectsIds () {

        List<Integer> ids = new ArrayList<>();

        for (ProjectDTO p : projects)
            ids.add(p.getId());

        return ids;
    }

    public List<String> getProjectsNames () {

        List<String> names = new ArrayList<>();

        for (ProjectDTO p : projects)
            names.add(p.getName());

        return names;
    }

    public void setProject(int id, String name) {

        this.project = new ProjectDTO(id, name);
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

}
