package backend.database.dao;

import backend.dto.IssueDTO;
import backend.dto.IssueStatusDTO;
import backend.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per il Data Access Object (DAO) relativo alla gestione delle segnalazioni (Issue).
 * <p>
 * Definisce il contratto per le operazioni di persistenza e recupero dati delle segnalazioni nel database.
 * Include metodi per la creazione (report), la ricerca avanzata, la consultazione di singole issue,
 * l'aggiornamento dello stato e l'assegnazione agli sviluppatori.
 * </p>
 */
public interface IssueDAO {

    /**
     * Registra una nuova segnalazione nel database.
     * <p>
     * Questo metodo si occupa di persistere i dati contenuti nel DTO fornito,
     * creando un nuovo record nella tabella delle issue.
     * </p>
     *
     * @param issueToReport Il DTO contenente tutte le informazioni della segnalazione da creare.
     * @throws SQLException In caso di errori durante l'operazione di inserimento nel database.
     */
    void reportIssue(IssueDTO issueToReport) throws SQLException;

    /**
     * Esegue una ricerca filtrata delle segnalazioni.
     * <p>
     * Permette di recuperare una lista di issue che corrispondono ai criteri specificati.
     * I filtri possono combinare attributi della issue (titolo, stato, ecc) e identificativi
     * degli utenti coinvolti (risolutore, reporter) o del progetto.
     * </p>
     *
     * @param issueToSearch Oggetto DTO usato come filtro per gli attributi della issue (es stato, tipo).
     * @param resolverId    (Opzionale) L'ID dello sviluppatore assegnato alla risoluzione.
     * @param reporterId    (Opzionale) L'ID dell'utente che ha aperto la segnalazione.
     * @param projectId     L'ID del progetto in cui effettuare la ricerca.
     * @return Una lista di {@code IssueDTO} che soddisfano i criteri di ricerca.
     * @throws SQLException In caso di errori durante l'esecuzione della query nel database.
     */
    List<IssueDTO> searchIssues(IssueDTO issueToSearch, Integer resolverId, Integer reporterId, Integer projectId) throws SQLException;

    /**
     * Recupera i dettagli completi di una singola segnalazione tramite il suo identificativo.
     *
     * @param issueId L'ID univoco della segnalazione da recuperare.
     * @return Il DTO popolato con i dati della segnalazione, oppure null se non trovata.
     * @throws SQLException In caso di errori di accesso al database.
     */
    IssueDTO getIssueById(Integer issueId) throws SQLException;

    /**
     * Aggiorna lo stato di avanzamento di una specifica segnalazione.
     *
     * @param id        L'ID della segnalazione da aggiornare.
     * @param newStatus Il nuovo stato da assegnare (es. OPEN, IN_PROGRESS, CLOSED).
     * @return {@code true} se l'aggiornamento è andato a buon fine, {@code false} altrimenti (es. ID non trovato).
     * @throws SQLException In caso di errori durante l'operazione di aggiornamento nel database.
     */
    boolean updateStatus(Integer id, IssueStatusDTO newStatus) throws SQLException;

    /**
     * Assegna una segnalazione a uno sviluppatore specifico identificato dalla sua email.
     * <p>
     * Questo metodo verifica l'esistenza dello sviluppatore e, se valido, aggiorna la segnalazione
     * impostando il campo del risolutore.
     * </p>
     *
     * @param id            L'ID della segnalazione da assegnare.
     * @param resolverEmail L'indirizzo email dello sviluppatore a cui assegnare la issue.
     * @return Il DTO dell'utente (sviluppatore) appena assegnato, utile per conferme lato client.
     * @throws SQLException In caso di errori nel database durante l'aggiornamento o la ricerca dell'utente.
     */
    UserDTO assignIssueToDeveloperByEmail(Integer id, String resolverEmail) throws SQLException;

}
