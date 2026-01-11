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

@SuppressWarnings("java:S6548")
public class ProjectController {

    private static ProjectController instance;
    private final ApiClient client = ApiClient.getInstance();

    private static final Logger logger = Logger.getLogger(ProjectController.class.getName());

    private ArrayList<ProjectDTO> projects;
    @Getter
    private ProjectDTO project;

    private StatisticDTO dashboardData;

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    private static final String PROJECTS_PATH = ApiPaths.PROJECTS;


    private ProjectController(){

    }

    public static ProjectController getInstance() {
        if (instance == null) {
            instance = new ProjectController();
        }
        return instance;
    }

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

    public void setProjectWithValues(int id, String name) {

        this.project = new ProjectDTO(id, name);
    }

    public void setProjectWithId(int id) {

        for(ProjectDTO p: projects)
            if(p.getId() == id){
                this.project = p;
                break;
            }


    }

    public List<String> getDevelopersEmails() {

        ArrayList<String> emails = new ArrayList<>();

        for(UserDTO dev : this.dashboardData.getDevelopers())
            emails.add(dev.getEmail());

        return emails;

    }

    public List<Integer> getOpenIssues() {
        return this.dashboardData.getNumOpenIssues();
    }

    public List<Integer> getResolvedIssues() {
        return this.dashboardData.getNumClosedIssues();
    }

    public List<Duration> getAverageResolvingDurations() {
        return this.dashboardData.getAverageResolutionDurations();
    }

    public int getTotalOpenIssues() {

        int total = 0;

        for (Integer i : this.dashboardData.getNumOpenIssues())
            total += i;

        return total;
    }

    public int getTotalResolvedIssues() {

        int total = 0;

        for (Integer i : this.dashboardData.getNumClosedIssues())
            total += i;

        return total;
    }

    public Duration getTotalAverageResolvingDuration() {
        return this.dashboardData.getTotalAverageResolutionDuration();
    }

    public Integer getNumIssuesNotAssigned(){
        return this.dashboardData.getNumIssuesNotAssigned();
    }

}
