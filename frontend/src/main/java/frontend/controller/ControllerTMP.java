package frontend.controller;

import frontend.dto.*;

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
        issuetmp.setTypeWithString("Bug");
        issuetmp.setPriority(3);
        issuetmp.setTags("tag1;tag2;tag3");
        issuetmp.setStatus(IssueStatusDTO.TODO);
        issuetmp.setReportDate(new Date("01/01/2016"));
        issuetmp.setResolutionDate(new Date("05/01/2016"));
        issuetmp.setReportingUser(new UserDTO("reportinguser@qualcosa", ""));
        issuetmp.setAssignedDeveloper(new UserDTO("assigneddeveloper@qualcosa", ""));

        IssueController.getInstance().setIssue(issuetmp);
    }




    public static void setIssueAsResolved() {

        /*
            todo:
            effettuare una query che imposti lo stato della issue indicata
            dell'attributo issue dell'IssueController a Resolved
            aggiorna pure i DTO dei controller
         */

    }

    public static List<String> searchDevelopers(String email) {

        /*
            todo:
            effettuare una query che restituisca le email di tutti i developers appartenenti al progetto
            presente nel ProjectController e la cui email contenga la stringa passata come parametro,
            nel caso non ce ne siano restituire una lista vuota
         */

        List<String> developers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            developers.add("developer " + i);
        }

        return developers;
    }

    public static void assignIssueToDeveloper(String email) {

        /*
            todo:
            effettuare una query che assegni l'issue presente in IssueController
            al developer con email uguale a quella passata come parametro
            aggiorna pure i DTO dei controller
         */
    }
}
