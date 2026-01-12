package backend.database.dao;

import backend.dto.TeamDTO;
import backend.dto.StatisticDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per il Data Access Object (DAO) relativo alla gestione dei team di progetto.
 * <p>
 * Definisce il contratto per le operazioni di persistenza sui gruppi di lavoro,
 * incluse la creazione, la ricerca, la gestione dei membri (aggiunta/rimozione)
 * e la generazione di report statistici periodici.
 * </p>
 */
public interface TeamDAO {

    /**
     * Cerca i team il cui nome corrisponde o contiene la stringa specificata, all'interno di un progetto.
     *
     * @param teamName  Il nome (o parte di esso) del team da cercare.
     * @param projectId L'identificativo del progetto a cui i team devono appartenere.
     * @return Una lista di {@code TeamDTO} che soddisfano i criteri di ricerca.
     * @throws SQLException In caso di errori durante l'accesso al database.
     */
    List<TeamDTO> searchTeamsByNameAndProject(String teamName, Integer projectId) throws SQLException;

    /**
     * Crea un nuovo team nel database.
     * <p>
     * Salva le informazioni del nuovo gruppo di lavoro (nome, progetto associato)
     * creando un record corrispondente nel sistema.
     * </p>
     *
     * @param teamToCreate Il DTO contenente i dati del team da creare.
     * @throws SQLException In caso di errori durante l'inserimento nel database.
     */
    void createTeam(TeamDTO teamToCreate) throws SQLException;

    /**
     * Aggiunge un utente specifico a un team esistente.
     * <p>
     * Associa l'utente identificato dall'email al team specificato,
     * permettendogli di collaborare alle attività del gruppo.
     * </p>
     *
     * @param teamId L'identificativo del team a cui aggiungere il membro.
     * @param email  L'indirizzo email dell'utente da aggiungere.
     * @return {@code true} se l'aggiunta ha successo, {@code false} se l'utente non esiste o è già presente.
     * @throws SQLException In caso di errori durante l'aggiornamento nel database.
     */
    boolean addMemberToTeam(Integer teamId, String email) throws SQLException;

    /**
     * Rimuove un utente da un team specifico.
     * <p>
     * Elimina l'associazione tra l'utente e il gruppo di lavoro.
     * </p>
     *
     * @param teamId L'identificativo del team da cui rimuovere il membro.
     * @param email  L'indirizzo email dell'utente da rimuovere.
     * @return {@code true} se la rimozione ha successo, {@code false} se il team o l'utente non vengono trovati.
     * @throws SQLException In caso di errori durante l'operazione nel database.
     */
    boolean removeMemberFromTeam(Integer teamId, String email) throws SQLException;

    /**
     * Genera un report statistico mensile per un determinato team.
     * <p>
     * Aggrega i dati sulle attività del team (es numero di issue risolte)
     * per il mese e l'anno specificati, restituendo un oggetto statistico.
     * </p>
     *
     * @param teamId L'identificativo del team oggetto del report.
     * @param month  Il mese di riferimento (es. "January", "01").
     * @param year   L'anno di riferimento (es. "2023").
     * @return Un oggetto {@code StatisticDTO} con i dati del report.
     * @throws SQLException In caso di errori durante l'elaborazione delle statistiche.
     */
    StatisticDTO generateMonthlyReport(Integer teamId, String month, String year) throws SQLException;

}
