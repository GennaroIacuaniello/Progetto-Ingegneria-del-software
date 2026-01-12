package backend.dto;

/**
 * Enumeration per definire le tipologie di segnalazione (Issue).
 * <p>
 * Utilizzato nel DTO {@link IssueDTO} per categorizzare la natura della segnalazione.
 * </p>
 */
public enum IssueTypeDTO {

    /**
     * Indica un difetto o un errore nel software.
     */
    BUG,

    /**
     * Indica una richiesta o un problema relativo alla documentazione.
     */
    DOCUMENTATION,

    /**
     * Indica una richiesta di nuova funzionalità.
     */
    FEATURE,

    /**
     * Indica una domanda generica o una richiesta di chiarimento.
     */
    QUESTION

}