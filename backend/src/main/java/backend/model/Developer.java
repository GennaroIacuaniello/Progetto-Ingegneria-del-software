package backend.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entità che rappresenta uno sviluppatore nel sistema.
 * <p>
 * Estende la classe {@link User}. A differenza della classe padre, qui vengono utilizzati
 * esplicitamente {@code @Getter} e {@code @Setter} per la gestione dei campi, senza generare
 * automaticamente i metodi {@code equals}, {@code hashCode} o {@code toString} aggiuntivi.
 * </p>
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Developer extends User {

    /**
     * Lista delle segnalazioni assegnate allo sviluppatore per la risoluzione.
     */
    private List<Issue> assignedIssues;

    /**
     * Lista dei progetti a cui lo sviluppatore è assegnato.
     */
    private List<Project> projects;

    /**
     * Lista dei team di cui lo sviluppatore è membro.
     */
    private List<Team> teams;

    /**
     * Costruttore completo per inizializzare un Developer.
     * <p>
     * Richiama il costruttore della superclasse {@link User} e inizializza
     * le liste specifiche (assignedIssues, projects, teams) gestendo eventuali valori null
     * per garantire che le liste non siano mai nulle dopo la costruzione.
     * </p>
     *
     * @param email          Email dell'utente.
     * @param hashedPassword Password hashata.
     * @param reportedIssues Lista delle issue segnalate.
     * @param assignedIssues Lista delle issue assegnate.
     * @param projects       Lista dei progetti.
     * @param teams          Lista dei team.
     */
    public Developer(String email, String hashedPassword, List<Issue> reportedIssues,
                     List<Issue> assignedIssues, List<Project> projects, List<Team> teams) {

        super(email, hashedPassword, reportedIssues);

        this.assignedIssues = new ArrayList<>();

        if(assignedIssues != null)
            this.assignedIssues.addAll(assignedIssues);

        this.projects = new ArrayList<>();

        if(projects != null)
            this.projects.addAll(projects);

        this.teams = new ArrayList<>();

        if(teams != null)
            this.teams.addAll(teams);

    }

}