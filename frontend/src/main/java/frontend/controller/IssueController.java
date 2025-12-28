package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import frontend.dto.IssueDTO;
import frontend.dto.IssueStatusDTO;
import frontend.dto.ProjectDTO;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueController {

    private static IssueController instance;
    private final ApiClient client = ApiClient.getInstance();


    private static List<IssueDTO> issues;
    private static IssueDTO issue;

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
            case "Molto Bassa" -> 0;
            case "Bassa" -> 1;
            case "Media" -> 2;
            case "Alta" -> 3;
            case "Molto Alta" -> 4;
            default -> -1;
        };
    }

}
