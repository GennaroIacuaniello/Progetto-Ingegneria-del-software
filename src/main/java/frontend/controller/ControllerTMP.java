package frontend.controller;

import backend.model.Project;
import backend.model.User;
import frontend.gui.ReportIssueUser;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

public class ControllerTMP {

    private static User user;
    private static List<Project> projects;
    private static Project project;

    public static void searchProjects(String query, String placeholder) {

        /*  todo:
            effettua una query che restituisce id e nome di tutti i progetti il cui nome contiene la stringa query,
            se query corrisponde a placeholder considerare la stringa vuota "" invece di query.
            I risultati della ricerca devono essere usati per creare una List<Project> da inserire come attributo in questa classe.
        */

        //behaviour per test (rimuovi dopo aver implementato versione Client-Server)
        projects = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            projects.add(new Project(valueOf(i).toString(), "progetto " + i));
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

    public static List<String> getProjectsIds () {

        List<String> ids = new ArrayList<>();

        for (Project p : projects)
            ids.add(p.getId());

        return ids;
    }

    public static List<String> getProjectsNames () {

        List<String> names = new ArrayList<>();

        for (Project p : projects)
            names.add(p.getName());

        return names;
    }

    public static void setProject(String id) {

        project =  new Project(id);
    }

    public static String getProject(String id) {
        return project.getId();
    }

}
