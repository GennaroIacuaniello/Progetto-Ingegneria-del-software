package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import frontend.dto.IssueDTO;
import frontend.dto.IssueStatusDTO;
import frontend.dto.ProjectDTO;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;

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

public class IssueController {

    private static IssueController instance;
    private final ApiClient client = ApiClient.getInstance();


    private ArrayList<IssueDTO> issues;
    private IssueDTO issue;

    private static final String RESOLVER_ID = "resolverId=";
    private static final String REPORTER_ID = "reporterId=";
    private static final String PROJECT_ID = "projectId=";

    private IssueController(){

    }

    public static IssueController getInstance() {
        if (instance == null) {
            instance = new IssueController();
        }
        return instance;
    }

    public void reportIssue(IssueDTO issueToReport, List<String> tags, File image) {

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
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {
                System.out.println("Issue reported successfully!");
            } else {
                System.err.println("Issue report failed. Code: " + response.statusCode());
                System.err.println("Error body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchIssueGeneral(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority, String roleToSearch) {

        List<String> params = setUpSearchParams(issueTitle, issueStatus, issueTags, issueType, issuePriority);

        params.add(roleToSearch + AuthController.getInstance().getLoggedUser().getId());
        params.add(PROJECT_ID + ProjectController.getInstance().getProject().getId());

        String queryString = String.join("&", params);

        HttpResponse<String> response = sendSearchRequest(queryString);

        handleSearchResponse(response);

    }

    public void searchAssignedIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        searchIssueGeneral(issueTitle, issueStatus, issueTags, issueType, issuePriority, RESOLVER_ID);

    }

    public void searchAllIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        List<String> params = setUpSearchParams(issueTitle, issueStatus, issueTags, issueType, issuePriority);

        params.add(PROJECT_ID + ProjectController.getInstance().getProject().getId());

        String queryString = String.join("&", params);

        HttpResponse<String> response = sendSearchRequest(queryString);

        handleSearchResponse(response);

    }

    public void searchReportedIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        searchIssueGeneral(issueTitle, issueStatus, issueTags, issueType, issuePriority, REPORTER_ID);

    }

    private List<String> setUpSearchParams(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority){

        List<String> params = new ArrayList<>();

        if (issueTitle != null && !issueTitle.isEmpty()) {
            //Encoder da utilizzare dato che non possono esserci spazi nei parametri search delle richieste HTTP
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

    private HttpResponse<String> sendSearchRequest(String queryString){

        String fullUrl = client.getBaseUrl() + "/issues/search";

        fullUrl += "?" + queryString;

        System.out.println("Calling URL: " + fullUrl); // Debug utile

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .GET();

        return client.sendRequest(requestBuilder);

    }

    private void handleSearchResponse(HttpResponse<String> response){

        try{

            if (response.statusCode() == 200) {

                this.issues = client.getObjectMapper().readValue(response.body(), new TypeReference<>(){});

            } else if (response.statusCode() == 204) {

                this.issues = new ArrayList<>();

            } else {

                // CASO ERRORE (500, 400, ecc.)
                System.err.println("Errore dal server. Codice: " + response.statusCode());
                System.err.println("Dettaglio errore: " + response.body());
            }

        } catch (RequestError re) {

            System.err.println("Backend offline: " + re.getMessage());
            this.issues = new ArrayList<>();

        } catch (Exception e) {

            e.printStackTrace();
            this.issues = new ArrayList<>();
        }

    }

    public void getIssueById() {

        try {

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/issues/" + issue.getId()))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.issue = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});

            } else if (response.statusCode() == 404) {

                System.err.println("Issue non trovata (ID: " + issue.getId() + ")");
                this.issue = null;

            } else {
                System.err.println("Errore server: " + response.statusCode());
                this.issue = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void setIssueAsResolved() {

        try {

            StatusUpdateRequest requestBody = new StatusUpdateRequest(IssueStatusDTO.RESOLVED);

            String jsonBody = client.getObjectMapper().writeValueAsString(requestBody);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/issues/" + issue.getId() + "/status"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {
                issue.setStatus(IssueStatusDTO.RESOLVED);
                System.out.println("Status update success: " + response.body());
            } else {
                System.err.println("Error in status update: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void assignIssueToDeveloper(String resolverEmail) {

        try {

            UserDTO resolver = new UserDTO();
            resolver.setEmail(resolverEmail);

            String jsonBody = client.getObjectMapper().writeValueAsString(resolver);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/issues/" + issue.getId() + "/resolver"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                UserDTO fullResolverInfo = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});

                issue.setAssignedDeveloper(fullResolverInfo);

                System.out.println("Issue assigned correctly to: " + fullResolverInfo.getEmail());

            } else {
                System.err.println("Error assigning issue. Code: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class StatusUpdateRequest {

        private IssueStatusDTO newStatus;

        public StatusUpdateRequest() {
            //Empty constructor needed for jackson
        }

        public StatusUpdateRequest(IssueStatusDTO newStatus) {
            this.newStatus = newStatus;
        }

        public IssueStatusDTO getNewStatus() {
            return newStatus;
        }

        public void setNewStatus(IssueStatusDTO newStatus) {
            this.newStatus = newStatus;
        }
    }

    public List<String> getIssuesTitles () {

        List<String> issuesTitles = new ArrayList<>();

        for (IssueDTO i : issues)
            issuesTitles.add(i.getTitle());

        return issuesTitles;
    }

    public String getIssueTitle() {
        return issue.getTitle();
    }

    public String getIssueDescription() {
        return issue.getDescription();
    }

    public String getIssueStatus() {
        return issue.getStatus().toString();
    }

    public String getIssuePriority() {

        return priorityIntToString(issue.getPriority());
    }

    public String getIssueType() {
        return issue.getType().toString();
    }

    public File getIssueImageAsFile() {

        try {
            File tempFile = File.createTempFile("issue_img_", ".jpg");


            Files.write(tempFile.toPath(), issue.getImage());

            tempFile.deleteOnExit();

            return tempFile;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getIssueTagsAsList () {

        String tagsString = issue.getTags();

        if (tagsString == null)
            tagsString = "";

        String[] tagsArray = tagsString.split(";");

        return Arrays.asList(tagsArray);

    }

    public Date getIssueResolutionDate () {
        return issue.getResolutionDate();
    }

    public Date getIssueReportDate () {
        return issue.getReportDate();
    }

    public UserDTO getIssueReportingUser () {
        return issue.getReportingUser();
    }

    public UserDTO getIssueAssignedDeveloper () {
        return issue.getAssignedDeveloper();
    }

    public void setIssue(IssueDTO i) {

        issue = i;

    }

    public IssueDTO getIssueFromIndex(int index) {

        return issues.get(index);
    }

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

}
