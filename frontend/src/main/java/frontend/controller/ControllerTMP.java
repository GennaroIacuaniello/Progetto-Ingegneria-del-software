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

    public static void reportIssue(ReportIssueUser reportIssue) {

        /*
            todo:
            effettua una query per reportare una Issue, le informazioni sulla Issue possono essere ricavate tramite l'oggetto reportIssue,
            aggiungere eventuali metodi mancanti per ottenere le informazioni nella classe ReportIssue (aattenzione agli Override),
            l'utente che ha riportato la Issue può essere trovaato nell'attributo user di questa classe (non ancora implementato pke manca tutta la logica di login)
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

    public static void searchReportedIssues(String title, String placeholder, String status, List<String> tags, String type, String priority) {

        /*  todo:
            effettua una query che restituisce di tutte le reported issues dell'utente corrente secondo i criteri di ricerca
            passati come parametri, se title corrisponde a placeholder considerare la stringa vuota "" invece di title,
            gli attributi possono essere null!
            I risultati della ricerca devono essere usati per creare una List<Issue> da inserire come attributo in questa classe.
        */

        //behaviour per test (rimuovi dopo aver implementato versione Client-Server)
        issues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            issues.add(new IssueDTO("issue " + i));
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

        String priority;

        switch (issue.getPriority()) {

            case 0:
                priority = "Molto Bassa";
                break;
            case 1:
                priority = "Bassa";
                break;
            case 2:
                priority = "Media";
                break;
            case 3:
                priority = "Alta";
            case 4:
                priority = "Molto Alta";
                break;
            default:
                priority = "Errore";
        }

        return priority;
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
}
