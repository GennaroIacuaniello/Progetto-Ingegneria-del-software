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

@SuppressWarnings("java:S6548")
public class IssueController {

    private static IssueController instance;

    private final ApiClient client = ApiClient.getInstance();

    private static final Logger logger = Logger.getLogger(IssueController.class.getName());

    private ArrayList<IssueDTO> issues;
    @Setter
    private IssueDTO issue;

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    private static final String ISSUES_PATH = ApiPaths.ISSUES;

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

    private boolean searchIssueGeneral(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority, String roleToSearch) {

        List<String> params = setUpSearchParams(issueTitle, issueStatus, issueTags, issueType, issuePriority);

        params.add(roleToSearch + AuthController.getInstance().getLoggedUser().getId());
        params.add(PROJECT_ID + ProjectController.getInstance().getProject().getId());

        String queryString = String.join("&", params);

        HttpResponse<String> response = sendSearchRequest(queryString);

        return handleSearchResponse(response);

    }

    public boolean searchAssignedIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        return searchIssueGeneral(issueTitle, issueStatus, issueTags, issueType, issuePriority, RESOLVER_ID);

    }

    public boolean searchAllIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        List<String> params = setUpSearchParams(issueTitle, issueStatus, issueTags, issueType, issuePriority);

        params.add(PROJECT_ID + ProjectController.getInstance().getProject().getId());

        String queryString = String.join("&", params);

        HttpResponse<String> response = sendSearchRequest(queryString);

        return handleSearchResponse(response);

    }

    public boolean searchReportedIssues(String issueTitle, String issueStatus, List<String> issueTags, String issueType, String issuePriority) {

        return searchIssueGeneral(issueTitle, issueStatus, issueTags, issueType, issuePriority, REPORTER_ID);

    }

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

    private HttpResponse<String> sendSearchRequest(String queryString){

        String fullUrl = client.getBaseUrl() + "/issues/search";

        fullUrl += "?" + queryString;

        logger.log(Level.FINE, "Calling URL: {0}", fullUrl);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .GET();

        return client.sendRequest(requestBuilder);

    }

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

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {

        private IssueStatusDTO newStatus;

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
            logger.log(Level.SEVERE, e.getMessage());
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
