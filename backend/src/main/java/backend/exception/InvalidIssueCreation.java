package backend.exception;

/**
 * Eccezione personalizzata lanciata quando la creazione di una segnalazione fallisce per dati non validi.
 * <p>
 * Utilizzata durante la validazione dei dati in ingresso (es. campi obbligatori mancanti,
 * formato errato) prima di procedere alla persistenza nel database.
 * </p>
 */
public class InvalidIssueCreation extends RuntimeException {

    /**
     * Costruttore che accetta un messaggio di errore descrittivo.
     *
     * @param message Il dettaglio del motivo per cui la creazione è fallita.
     */
    public InvalidIssueCreation(String message) {
        super(message);
    }
}