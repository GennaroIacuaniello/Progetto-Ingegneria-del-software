package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Date;

/**
 * Entità che rappresenta una segnalazione (Issue) all'interno del sistema.
 * <p>
 * Contiene tutti i dettagli operativi per tracciare un bug o una richiesta.
 * Utilizza l'annotazione {@code @Data} per generare automaticamente getter, setter,
 * equals, hashCode e toString.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    /**
     * Titolo breve della segnalazione.
     */
    private String title;

    /**
     * Descrizione dettagliata del problema.
     */
    private String description;

    /**
     * Tipo di segnalazione (es. BUG, FEATURE).
     */
    private IssueType type;

    /**
     * Stato corrente della segnalazione (es ASSIGNED, RESOLVED).
     */
    private IssueStatus status;

    /**
     * Lista di tag per la categorizzazione.
     */
    private List<String> tags;

    /**
     * Data di apertura della segnalazione.
     */
    private Date reportDate;

    /**
     * Data di chiusura/risoluzione (può essere null).
     */
    private Date resolutionDate;

    /**
     * Utente che ha aperto la segnalazione.
     */
    private User reportingUser;

    /**
     * Progetto a cui la segnalazione appartiene.
     */
    private Project relatedProject;

    /**
     * Livello di priorità (valore intero).
     */
    private int priority;

    /**
     * Immagine allegata (array di byte).
     */
    private byte[] image;

    /**
     * Sviluppatore assegnato alla risoluzione.
     */
    private Developer assignedDeveloper;

}