package frontend.controller;

import frontend.dto.*;
import frontend.gui.ReportIssueUser;

import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

public class ControllerTMP {

    private ProjectController projectController;

    private static UserDTO user;
    private static List<ProjectDTO> projects;
    private static ProjectDTO project;
    private static List<IssueDTO> issues;
    private static IssueDTO issue;

    public static void searchProjects(String query, String placeholder) {

        /*  todo:
            effettua una query che restituisce id e nome di tutti i progetti il cui nome contiene la stringa query,
            se query corrisponde a placeholder considerare la stringa vuota "" invece di query.
            I risultati della ricerca devono essere usati per creare una List<Project> da inserire come attributo in questa classe.
        */

        //behaviour per test (rimuovi dopo aver implementato versione Client-Server)
        projects = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            projects.add(new ProjectDTO(i, "progetto " + i));
        }
    }

    public static void reportIssue(IssueDTO issue) {

        /*
            todo:
            effettua una query per reportare una Issue, le informazioni sulla Issue possono essere ricavate tramite l'oggetto issue:
            - titolo: sempre inserito
            - descrizione: sempre inserita (potrebbe essere stringa vuota)
            - type: sempre inserito
            - status: devi mettere a to do
            - priority: sempre inserita
            - image: potrebbe essere null
            - tags: List<String> != null, ma può essere vuoto, che devi mergare
            - date: le ricavi tu
            - utenti: li ricavi tu (loggedUser)
            _ projectId: lo ricavi tu (project di ProjectController)
         */
    }

    public static List<Integer> getProjectsIds () {

        List<Integer> ids = new ArrayList<>();

        for (ProjectDTO p : projects)
            ids.add(p.getId());

        return ids;
    }

    public static List<String> getProjectsNames () {

        List<String> names = new ArrayList<>();

        for (ProjectDTO p : projects)
            names.add(p.getName());

        return names;
    }

    public static void setProject(int id, String name) {

        project =  new ProjectDTO(id, name);
    }

    public static int getProject(int id) {
        return project.getId();
    }

    public static void searchReportedIssues(String title, String status, List<String> tags, String type, String priority) {

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

    public static void searchAssignedIssues(String title, String status, List<String> tags, String type, String priority) {

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

    public static void searchAllIssues(String title, String status, List<String> tags, String type, String priority) {

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

    public static List<String> getIssuesTitles () {

        List<String> issuesTitles = new ArrayList<>();

        for (IssueDTO i : issues)
            issuesTitles.add(i.getTitle());

        return issuesTitles;
    }

    public static String getIssueTitle() {
        return issue.getTitle();
    }

    public static String getIssueDescription() {
        return issue.getDescription();
    }

    public static String getIssueStatus() {
        return issue.getStatus().toString();
    }

    public static String getIssuePriority() {

        return PriorityConverter.intToString(issue.getPriority());
    }

    public static String getIssueType() {
        return issue.getType().toString();
    }

    public static File getIssueImage() {
        return issue.getImage();
    }

    public static List<String> getIssueTags () {
        return issue.getTags();
    }

    public static Date getIssueResolutionDate () {
        return issue.getResolutionDate();
    }

    public static Date getIssueReportDate () {
        return issue.getReportDate();
    }

    public static UserDTO getIssueReportingUser () {
        return issue.getReportingUser();
    }

    public static UserDTO getIssueAssignedDeveloper () {
        return issue.getAssignedDeveloper();
    }

    public static void setIssue(IssueDTO i) {

        issue = i;
    }

    public static IssueDTO getIssueFromIndex(int index) {

        return issues.get(index);
    }

    public static void setIssueDetails() {

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

}
