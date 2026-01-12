package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object (DTO) per rappresentare una segnalazione (Issue).
 * <p>
 * Questa classe funge da contenitore per trasferire i dati delle segnalazioni tra i vari livelli dell'applicazione
 * (Controller, Service, DAO) e verso il frontend. Include tutte le informazioni che descrivono una issue,
 * dal titolo e descrizione fino allo stato, alle date di gestione e agli utenti coinvolti.
 * </p>
 * <p>
 * <strong>Utilizzo di Lombok:</strong><br>
 * Per ridurre la verbosità del codice ("boilerplate code"), questa classe sfrutta le seguenti annotazioni:
 * <ul>
 * <li>{@link Data @Data}: Genera automaticamente i metodi <strong>getter</strong> e <strong>setter</strong> per tutti i campi,
 * oltre all'implementazione standard dei metodi {@code toString()}, {@code equals()} e {@code hashCode()}.</li>
 * <li>{@link NoArgsConstructor @NoArgsConstructor}: Genera un <strong>costruttore vuoto</strong> (senza argomenti),
 * fondamentale per la serializzazione/deserializzazione JSON e per l'istanziazione tramite framework.</li>
 * <li>{@link AllArgsConstructor @AllArgsConstructor}: Genera un <strong>costruttore completo</strong> che accetta
 * come argomenti tutti i campi della classe, utile per creare istanze popolate in un'unica istruzione.</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO {

    /**
     * Identificativo univoco della segnalazione.
     * Generato automaticamente dal database al momento della creazione.
     */
    private Integer id;

    /**
     * Titolo breve e descrittivo della segnalazione.
     */
    private String title;

    /**
     * Descrizione dettagliata del problema o della richiesta.
     */
    private String description;

    /**
     * Tipologia della segnalazione (es. BUG, FEATURE_REQUEST).
     * Definito dall'enumeration {@link IssueTypeDTO}.
     */
    private IssueTypeDTO type;

    /**
     * Stato corrente della lavorazione (es. OPEN, IN_PROGRESS, CLOSED).
     * Definito dall'enumeration {@link IssueStatusDTO}.
     */
    private IssueStatusDTO status;

    /**
     * Stringa contenente i tag associati alla segnalazione.
     * Utile per la categorizzazione e la ricerca (es "backend, login, urgente").
     */
    private String tags;

    /**
     * Data e ora di creazione della segnalazione.
     */
    private Date reportDate;

    /**
     * Data e ora di risoluzione/chiusura della segnalazione.
     * Può essere {@code null} se la issue è ancora aperta.
     */
    private Date resolutionDate;

    /**
     * L'utente che ha aperto la segnalazione (Reporter).
     * Contiene le informazioni dell'utente (es email, id).
     */
    private UserDTO reportingUser;

    /**
     * Il progetto a cui la segnalazione fa riferimento.
     */
    private ProjectDTO relatedProject;

    /**
     * Livello di priorità della segnalazione.
     * Solitamente rappresentato da un valore numerico (es. 1 = Bassa, 3 = Alta).
     */
    private Integer priority;

    /**
     * Immagine allegata alla segnalazione (es screenshot del bug).
     * Memorizzata come array di byte (BLOB).
     */
    private byte[] image;

    /**
     * Lo sviluppatore assegnato alla risoluzione della segnalazione (Resolver).
     * Può essere {@code null} se la issue non è ancora stata presa in carico.
     */
    private UserDTO assignedDeveloper;

}