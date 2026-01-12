package backend.database.dao;

import backend.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per il Data Access Object (DAO) relativo alla gestione degli utenti.
 * <p>
 * Definisce il contratto per le operazioni di persistenza sugli utenti,
 * incluse la registrazione di nuovi account e le varie modalità di ricerca
 * (per email, per progetto o per team) necessarie per le funzionalità di amministrazione e assegnazione.
 * </p>
 */
public interface UserDAO {

    /**
     * Registra un nuovo utente nel database.
     * <p>
     * Persiste i dati del nuovo utente (email, password hashata, ruolo)
     * creando un record corrispondente nel sistema.
     * </p>
     *
     * @param newUser Il DTO contenente i dati dell'utente da registrare.
     * @throws SQLException In caso di errori durante l'inserimento nel database.
     */
    void registerNewUser(UserDTO newUser) throws SQLException;

    /**
     * Cerca un utente specifico tramite il suo indirizzo email.
     * <p>
     * Utilizzato principalmente in fase di login per recuperare le credenziali
     * o in fase di registrazione per verificare che l'email non sia già in uso.
     * </p>
     *
     * @param email L'indirizzo email dell'utente da cercare.
     * @return Il {@code UserDTO} corrispondente se trovato, altrimenti {@code null}.
     * @throws SQLException In caso di errori durante l'accesso al database.
     */
    UserDTO searchUserByMail(String email) throws SQLException;

    /**
     * Cerca utenti con ruolo Sviluppatore o Amministratore filtrati per email e progetto.
     * <p>
     * Restituisce una lista di utenti che partecipano allo specifico progetto
     * e la cui email corrisponde al criterio di ricerca.
     * </p>
     *
     * @param email     L'email (o parte di essa) da cercare.
     * @param projectId L'identificativo del progetto in cui cercare.
     * @return Una lista di {@code UserDTO} che soddisfano i criteri.
     * @throws SQLException In caso di errori durante la ricerca nel database.
     */
    List<UserDTO> searchDevOrAdminByEmailAndProject(String email, Integer projectId) throws SQLException;

    /**
     * Cerca utenti con ruolo Sviluppatore o Amministratore filtrati per email e team.
     * <p>
     * Utile per trovare candidati da aggiungere a un team o per cercare tra i membri esistenti,
     * a seconda della logica implementativa specifica.
     * </p>
     *
     * @param email  L'email (o parte di essa) da cercare.
     * @param teamId L'identificativo del team di riferimento.
     * @return Una lista di {@code UserDTO} che soddisfano i criteri.
     * @throws SQLException In caso di errori durante la ricerca nel database.
     */
    List<UserDTO> searchDevOrAdminByEmailAndTeam(String email, Integer teamId) throws SQLException;

    /**
     * Cerca globalmente utenti con ruolo Sviluppatore o Amministratore tramite email.
     * <p>
     * Esegue una ricerca nell'intero database senza restrizioni di progetto o team.
     * </p>
     *
     * @param email L'email (o parte di essa) da cercare.
     * @return Una lista di {@code UserDTO} trovati.
     * @throws SQLException In caso di errori durante l'accesso al database.
     */
    List<UserDTO> searchDevOrAdminByEmail(String email) throws SQLException;

}
