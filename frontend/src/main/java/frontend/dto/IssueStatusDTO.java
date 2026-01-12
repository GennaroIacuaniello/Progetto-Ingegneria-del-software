package frontend.dto;

/**
 * Enumeration per definire gli stati di avanzamento di una segnalazione (Issue).
 * <p>
 * Utilizzato nel DTO {@link IssueDTO} per tracciare il ciclo di vita della issue.
 * </p>
 */
public enum IssueStatusDTO {

    /**
     * La segnalazione è stata assegnata a uno sviluppatore ma non è ancora risolta.
     */
    ASSIGNED,

    /**
     * La segnalazione è stata risolta e chiusa.
     */
    RESOLVED,

    /**
     * La segnalazione è stata aperta ma non è ancora stata presa in carico o assegnata.
     */
    TODO

}