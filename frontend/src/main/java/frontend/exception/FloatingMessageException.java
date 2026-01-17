package frontend.exception;

/**
 * Eccezione personalizzata per errori relativi ai messaggi fluttuanti (Floating Message).
 * <p>
 * Questa eccezione estende {@link RuntimeException} e viene lanciata quando si verificano problemi
 * nella creazione, visualizzazione o gestione dei messaggi temporanei di notifica (toast) nell'interfaccia utente.
 * Può essere utilizzata, ad esempio, se si tenta di mostrare un messaggio in un contesto non valido
 * o con parametri errati.
 * </p>
 */
public class FloatingMessageException extends RuntimeException {

    /**
     * Costruttore che accetta un messaggio descrittivo dell'errore.
     *
     * @param message Il dettaglio del motivo per cui l'eccezione è stata lanciata.
     */
    public FloatingMessageException(String message) {
        super(message);
    }

}