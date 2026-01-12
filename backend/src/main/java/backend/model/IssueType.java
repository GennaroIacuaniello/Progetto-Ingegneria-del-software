package backend.model;

/**
 * Enumeration per definire le tipologie di segnalazione nel modello di dominio.
 */
public enum IssueType {

    /**
     * Errore software.
     */
    BUG,

    /**
     * Problema o richiesta sulla documentazione.
     */
    DOCUMENTATION,

    /**
     * Richiesta di nuova funzionalità.
     */
    FEATURE,

    /**
     * Domanda generica.
     */
    QUESTION
}