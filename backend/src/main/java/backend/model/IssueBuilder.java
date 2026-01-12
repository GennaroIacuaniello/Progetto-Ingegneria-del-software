package backend.model;

import backend.exception.InvalidIssueCreation;

import java.util.Date;
import java.util.List;

/**
 * Builder per la creazione controllata di oggetti {@link Issue}.
 * <p>
 * Permette di costruire un'istanza passo dopo passo e di validare la consistenza dei dati
 * (es presenza dei campi obbligatori) prima di restituire l'oggetto finale tramite il metodo {@link #build()}.
 * </p>
 */
@SuppressWarnings("unused")
public class IssueBuilder {

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
    private int priority = 2; // Valore di default

    /**
     * Immagine allegata (array di byte).
     */
    private byte[] image = null;

    /**
     * Sviluppatore assegnato alla risoluzione.
     */
    private Developer assignedDeveloper = null;

    /**
     * Costruttore vuoto necessario per la compatibilità con librerie di serializzazione (es. Jackson).
     */
    public IssueBuilder() {
        //Empty constructor needed for jackson
    }

    /**
     * Imposta il titolo della issue.
     * @param title Il titolo.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Imposta la descrizione della issue.
     * @param description La descrizione.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Imposta il tipo della issue.
     * @param type Il tipo (enum).
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withType(IssueType type) {
        this.type = type;
        return this;
    }

    /**
     * Imposta lo stato della issue.
     * @param status Lo stato (enum).
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withStatus(IssueStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Imposta i tag della issue.
     * @param tags Lista di stringhe.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Imposta le date di report e risoluzione.
     * @param reportDate Data di creazione.
     * @param resolutionDate Data di risoluzione.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withDates(Date reportDate, Date resolutionDate) {
        this.reportDate = reportDate;
        this.resolutionDate = resolutionDate;
        return this;
    }

    /**
     * Imposta l'utente reporter.
     * @param reportingUser L'utente che segnala.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
        return this;
    }

    /**
     * Imposta il progetto correlato.
     * @param relatedProject Il progetto.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
        return this;
    }

    /**
     * Imposta la priorità.
     * @param priority Intero rappresentante la priorità.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Imposta l'immagine allegata.
     * @param image Array di byte.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withImage(byte[] image) {
        this.image = image;
        return this;
    }

    /**
     * Imposta lo sviluppatore assegnato.
     * @param assignedDeveloper Lo sviluppatore.
     * @return L'istanza corrente del builder.
     */
    public IssueBuilder withAssignedDeveloper(Developer assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
        return this;
    }

    /**
     * Costruisce e restituisce l'istanza finale di {@link Issue}.
     * <p>
     * Esegue una validazione dei campi obbligatori. Se uno dei campi essenziali è mancante,
     * lancia un'eccezione {@link InvalidIssueCreation}.
     * </p>
     *
     * @return L'oggetto Issue popolato.
     * @throws InvalidIssueCreation Se mancano dati obbligatori.
     */
    public Issue build() {

        if (title == null || description == null || type == null ||
                status == null || tags == null || reportDate == null ||
                reportingUser == null || relatedProject == null) {

            throw new InvalidIssueCreation("Issue creation failed: some mandatory fields are missing.");
        }

        return new Issue(title, description, type, status, tags,
                reportDate, resolutionDate, reportingUser, relatedProject,
                priority, image, assignedDeveloper);
    }
}