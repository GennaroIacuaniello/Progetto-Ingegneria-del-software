package backend.model;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entità che rappresenta un amministratore del sistema.
 * <p>
 * Estende {@link Developer}. Utilizza l'annotazione {@code @AllArgsConstructor} di Lombok
 * per generare un costruttore che accetta tutti i campi ereditati, utile per l'istanziazione rapida.
 * Non definisce nuovi campi ma eredita comportamento e struttura da Developer.
 * </p>
 */
@AllArgsConstructor
public class Admin extends Developer{

    /**
     * Costruttore che richiama il costruttore della superclasse {@link Developer}.
     * <p>
     * L'annotazione {@code @SuppressWarnings("unused")} suggerisce che questo costruttore
     * potrebbe non essere invocato direttamente nel codice sorgente, ma utilizzato tramite
     * framework o per completezza.
     * </p>
     *
     * @param email          Email dell'admin.
     * @param hashedPassword Password hashata.
     * @param reportedIssues Issue segnalate.
     * @param assignedIssue  Issue assegnate.
     * @param projects       Progetti gestiti/associati.
     * @param teams          Team gestiti/associati.
     */
    @SuppressWarnings("unused")
    public Admin (String email, String hashedPassword, List<Issue> reportedIssues,
                  List<Issue> assignedIssue, List<Project> projects, List<Team> teams) {

        super(email, hashedPassword, reportedIssues, assignedIssue, projects, teams);
    }

}