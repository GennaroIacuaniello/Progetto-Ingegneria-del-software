package backend.model;

/**
 * Enumeration per definire gli stati possibili di una segnalazione nel modello di dominio.
 */
public enum IssueStatus {

    /**
     * Assegnata a uno sviluppatore.
     */
    ASSIGNED,

    /**
     * Risolta e chiusa.
     */
    RESOLVED,

    /**
     * Da fare (stato iniziale o non ancora assegnata).
     */
    TODO
}