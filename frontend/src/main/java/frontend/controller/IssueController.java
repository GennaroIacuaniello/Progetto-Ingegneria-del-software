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








    public void searchAssignedIssues(String title, String status, List<String> tags, String type, String priority) {

        /*  todo:
            effettuare una query che restituisce le assignedIssue del loggedUser relative al project in ProjectController,
            filtrate secondo i criteri passati come parametri,
            di tali issue sono richiesti le chiavi primarie e i titoli.
            Parametri:
            - title: può essere stringa vuota
            - status: != null
            - tags: List<String> != null, ma può essere vuota (io passo tante stringhe, nel DB c'è la stringa unica)
            - type: != null
            - priority: != null
            I risultati della query devono essere usati per creare una List<IssueDTO> da mettere come attributo al controller corrispondente
        */

        //behaviour per test (rimuovi dopo aver implementato versione Client-Server)
        issues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            issues.add(new IssueDTO(i, "issue " + i));
        }
    }

    public void searchAllIssues(String title, String status, List<String> tags, String type, String priority) {

        /*  todo:
            effettuare una query che restituisce  tutte le issue relative al project in ProjectController
            filtrate secondo i criteri passati come parametri,
            di tali issue sono richiesti le chiavi primarie e i titoli.
            Parametri:
            - title: può essere stringa vuota
            - status: != null
            - tags: List<String> != null, ma può essere vuota (io passo tante stringhe, nel DB c'è la stringa unica)
            - type: != null
            - priority: != null
            I risultati della query devono essere usati per creare una List<IssueDTO> da mettere come attributo al controller corrispondente
        */

        //behaviour per test (rimuovi dopo aver implementato versione Client-Server)
        issues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            issues.add(new IssueDTO(i, "issue " + i));
        }
    }


    public void setIssueDetails() {

        /*
            Questo metodo deve settare tutti gli attributi della issue che si trova nel controller
            (attualmente è questa classe, poi diventerà IssueController)
            Attributi:
            - title: c'è obbligatoriamente
            - description: visto che io ti passo le stringhe vuote se non c'è
                           la descrizione mi aspetto che tu fai lo stesso
            - type: c'è obbligatoriamente
            - tags: me li aspetto separati in un List<String>, se non ci
                    sono tags mi aspetto la List<String> vuota ma non null
            - image: mi aspetto l'immagine di tipo File (quando le metterai nel DB vedremo se
                     effettivamente va bene o devo cambiare io qualcosa nel FileChooser) oppure null
            - status: c'è obbligatoriamente
            - reportDate: c'è obbligatoriamente
            - resolutionDate: se non è stato ancora risolto mi aspetto null
                              (se preferisci stringa vuota va bene basta che mi fai sapere)
            - reportingUser: c'è obbligatoriamente
            - assignedDeveloper: se non c'è mi aspetto null (se preferisci stringa
                                 vuota va bene basta che mi fai sapere)
            - priority: c'è obbligatoriamente
         */
    }


    public void searchReportedIssues(String title, String status, List<String> tags, String type, String priority) {

        /*  todo:
            effettuare una query che restituisce le reportedIssue del loggedUser relative al project in ProjectController,
            filtrate secondo i criteri passati come parametri,
            di tali issue sono richiesti le chiavi primarie e i titoli.
            Parametri:
            - title: può essere stringa vuota
            - status: != null
            - tags: List<String> != null, ma può essere vuota (io passo tante stringhe, nel DB c'è la stringa unica)
            - type: != null
            - priority: può essere null
            I risultati della query devono essere usati per creare una List<IssueDTO> da mettere come attributo al controller corrispondente
        */

        //behaviour per test (rimuovi dopo aver implementato versione Client-Server)
        issues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            issues.add(new IssueDTO(i, "issue " + i));
        }
    }
}
