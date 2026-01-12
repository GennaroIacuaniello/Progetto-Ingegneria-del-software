package backend.exception;

/**
 * Eccezione personalizzata lanciata per operazioni non valide relative ai team.
 * <p>
 * Può indicare situazioni come il tentativo di aggiungere un membro a un team inesistente,
 * la duplicazione di un team, o violazioni delle regole di business legate alla gestione dei gruppi.
 * </p>
 */
public class InvalidTeam extends RuntimeException {

    /**
     * Costruttore che accetta un messaggio di errore descrittivo.
     *
     * @param message Il dettaglio dell'errore riscontrato.
     */
    public InvalidTeam(String message) {
        super(message);
    }

}