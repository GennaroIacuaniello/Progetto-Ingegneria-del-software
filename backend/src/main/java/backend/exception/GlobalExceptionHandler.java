package backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

/**
 * Gestore globale delle eccezioni per l'applicazione REST.
 * <p>
 * Annotata con {@link RestControllerAdvice @RestControllerAdvice}, questa classe intercetta le eccezioni
 * lanciate dai controller e le trasforma in risposte HTTP strutturate (oggetti {@link ErrorResponse}),
 * evitando che stack trace grezzi vengano esposti al client.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Logger per la registrazione degli errori, utile per il debugging e il monitoraggio.
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Gestisce le eccezioni generiche non catturate da handler più specifici.
     * <p>
     * Intercetta qualsiasi {@link Exception} e restituisce una risposta con stato 500 (Internal Server Error).
     * </p>
     *
     * @param e L'eccezione generica catturata.
     * @return ResponseEntity contenente i dettagli dell'errore.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {

        ErrorResponse error = new ErrorResponse(
                "Error: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    /**
     * Gestisce le eccezioni di tipo {@link ResponseStatusException}.
     * <p>
     * Queste eccezioni sono spesso lanciate intenzionalmente nei controller (es. 404 Not Found, 401 Unauthorized).
     * L'handler preserva il codice di stato e il messaggio (reason) originali dell'eccezione.
     * </p>
     *
     * @param e L'eccezione ResponseStatusException catturata.
     * @return ResponseEntity con lo stato e il messaggio specifici dell'eccezione.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e) {

        ErrorResponse error = new ErrorResponse(
                e.getReason(),
                e.getStatusCode().value()
        );

        return ResponseEntity
                .status(e.getStatusCode())
                .body(error);
    }

    /**
     * Gestisce le eccezioni relative al database (SQL).
     * <p>
     * Intercetta errori come problemi di connessione o query malformate.
     * Registra l'errore nel log per l'analisi lato server, ma restituisce al client un messaggio generico
     * "Database error" per non esporre dettagli sensibili sulla struttura del DB.
     * </p>
     *
     * @param e L'eccezione SQLException catturata.
     * @return ResponseEntity con stato 500 (Internal Server Error).
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException e) {

        String errorMessage = "Database error";

        logger.error(errorMessage, e);

        ErrorResponse error = new ErrorResponse(
                errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR.value() // 500
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}