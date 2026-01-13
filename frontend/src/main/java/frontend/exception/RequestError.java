package frontend.exception;

/**
 * Eccezione personalizzata per segnalare errori generici durante le richieste HTTP.
 * <p>
 * Questa eccezione estende {@link RuntimeException} e viene utilizzata nel frontend
 * per interrompere il flusso di esecuzione quando si verifica un problema di rete o logico
 * durante una chiamata API (es timeout, server offline).
 * </p>
 * <p>
 * <strong>Ottimizzazione:</strong><br>
 * Sovrascrive il metodo {@link #fillInStackTrace()} restituendo {@code this} invece di popolare
 * lo stack trace completo. Questo migliora notevolmente le performance, poiché l'eccezione
 * è usata per il controllo del flusso e non necessita delle informazioni di debug dello stack.
 * </p>
 */
public class RequestError extends RuntimeException {

    /**
     * Costruttore vuoto.
     */
    public RequestError(){}

    /**
     * Costruttore con messaggio descrittivo.
     *
     * @param message Il messaggio di errore.
     */
    public RequestError(String message) {
        super(message);
    }

    /**
     * Sovrascrittura per disabilitare la generazione dello stack trace.
     * <p>
     * Rende l'istanziazione dell'eccezione molto più leggera e veloce,
     * ideale per eccezioni usate frequentemente per il controllo del flusso.
     * </p>
     *
     * @return L'istanza stessa dell'eccezione, senza stack trace popolato.
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
