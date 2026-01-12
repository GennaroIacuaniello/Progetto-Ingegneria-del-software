package backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Classe che definisce la struttura standard delle risposte di errore dell'API.
 * <p>
 * Viene utilizzata per inviare al client informazioni strutturate (messaggio, timestamp e codice stato)
 * quando si verifica un'eccezione, garantendo un formato di risposta uniforme in tutta l'applicazione.
 * </p>
 * <p>
 * <strong>Utilizzo di Lombok:</strong><br>
 * Le annotazioni {@link Getter @Getter} e {@link Setter @Setter} generano automaticamente
 * i metodi di accesso e modifica per i campi privati.
 * </p>
 */
@Setter
@Getter
public class ErrorResponse {

    /**
     * Messaggio descrittivo dell'errore.
     */
    private String message;

    /**
     * Timestamp che indica il momento esatto in cui si è verificato l'errore.
     * Inizializzato automaticamente nel costruttore.
     */
    private LocalDateTime data;

    /**
     * Codice di stato HTTP associato all'errore (es. 400, 404, 500).
     */
    private int codiceStatus;

    /**
     * Costruttore per creare una nuova risposta di errore.
     * <p>
     * Imposta il messaggio e il codice di stato forniti, e inizializza automaticamente
     * il campo {@code data} con l'orario corrente.
     * </p>
     *
     * @param message      Il messaggio di errore da visualizzare.
     * @param codiceStatus Il codice HTTP di stato.
     */
    public ErrorResponse(String message, int codiceStatus) {
        this.message = message;
        this.codiceStatus = codiceStatus;
        this.data = LocalDateTime.now();
    }

}