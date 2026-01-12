package backend.exception;

/**
 * Eccezione personalizzata lanciata quando un'operazione coinvolge uno sviluppatore non valido.
 * <p>
 * Può essere utilizzata, ad esempio, quando si tenta di assegnare una issue a un utente che non ha
 * il ruolo di sviluppatore, o quando l'ID fornito non corrisponde ad alcuno sviluppatore esistente.
 * </p>
 */
public class InvalidDeveloper extends RuntimeException {

    /**
     * Costruttore che accetta un messaggio di errore descrittivo.
     *
     * @param message Il dettaglio dell'errore.
     */
    public InvalidDeveloper(String message) {
        super(message);
    }

}