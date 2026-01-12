package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entità base che rappresenta un utente generico nel sistema.
 * <p>
 * Questa classe funge da padre per le specializzazioni {@link Developer} e {@link Admin}.
 * Utilizza {@code @Data} di Lombok per generare automaticamente getter, setter,
 * equals, hashCode e toString, fornendo un oggetto completo per la gestione dei dati utente.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Indirizzo email dell'utente, utilizzato come identificativo univoco.
     */
    private String email;

    /**
     * Password dell'utente.
     */
    private String password;

    /**
     * Lista delle segnalazioni (Issue) aperte da questo utente.
     * Inizializzata come ArrayList.
     */
    private ArrayList<Issue> reportedIssues;

    /**
     * Costruttore personalizzato per inizializzare un utente.
     * <p>
     * Gestisce l'inizializzazione della lista {@code reportedIssues} per evitare NullPointerException,
     * copiando i dati se forniti o creando una lista vuota.
     * </p>
     *
     * @param email          L'indirizzo email dell'utente.
     * @param password       La password dell'utente.
     * @param reportedIssues Lista delle issue segnalate (può essere null).
     */
    public User(String email, String password, List<Issue> reportedIssues) {

        this.email = email;
        this.password = password;

        this.reportedIssues = new ArrayList<>();

        if(reportedIssues != null)
            this.reportedIssues.addAll(reportedIssues);

    }

}