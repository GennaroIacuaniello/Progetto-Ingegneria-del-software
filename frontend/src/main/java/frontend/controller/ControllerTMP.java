package frontend.controller;

import frontend.dto.*;
import frontend.gui.ReportIssueUser;

import java.awt.*;
import java.io.File;
import java.util.Date;
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

        IssueDTO issuetmp = new IssueDTO();

        issuetmp.setTitle("titolo issue");
        issuetmp.setDescription("descrizione");
        issuetmp.setType("Bug");
        issuetmp.setPriority(3);
        issuetmp.setTags("tag1;tag2;tag3");
        issuetmp.setStatus(IssueStatusDTO.ASSIGNED);
        issuetmp.setReportDate(new Date("01/01/2016"));
        issuetmp.setResolutionDate(new Date("05/01/2016"));
        issuetmp.setReportingUser(new UserDTO("reportinguser@qualcosa", ""));
        issuetmp.setAssignedDeveloper(new UserDTO("assigneddeveloper@qualcosa", ""));

        IssueController.getInstance().setIssue(issuetmp);
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
}
