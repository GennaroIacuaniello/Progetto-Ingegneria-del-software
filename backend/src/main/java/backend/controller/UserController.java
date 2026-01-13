package backend.controller;

import backend.database.dao.UserDAO;
import backend.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller REST per la gestione delle operazioni sugli utenti.
 * <p>
 * Questa classe espone gli endpoint per la ricerca di utenti specifici (Sviluppatori o Amministratori),
 * utile per operazioni di assegnazione (es assegnare una issue o aggiungere membri a un team).
 * Tutti gli endpoint sono mappati sotto il percorso base "/users".
 * </p>
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Componente di accesso ai dati (DAO) per le operazioni sugli utenti.
     * Utilizzato per eseguire le query di ricerca nel database in base ai filtri specificati.
     */
    private final UserDAO userDAO;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param userDAO Il DAO per la gestione degli utenti.
     */
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Cerca utenti con ruolo Sviluppatore o Amministratore tramite email.
     * <p>
     * La ricerca può essere contestualizzata a un progetto o a un team specifico, oppure essere globale.
     * La logica di filtraggio segue questo ordine di priorità:
     * </p>
     * <ul>
     * <li>Se è presente {@code projectId}: cerca tra i membri associati a quel progetto.</li>
     * <li>Altrimenti, se è presente {@code teamId}: cerca tra i candidati validi per quel team (es membri del progetto non ancora nel team).</li>
     * <li>Altrimenti: esegue una ricerca globale nel sistema.</li>
     * </ul>
     *
     * @param email     L'email (o parte di essa) da cercare. Obbligatoria.
     * @param projectId (Opzionale) L'ID del progetto in cui limitare la ricerca.
     * @param teamId    (Opzionale) L'ID del team per cui cercare possibili membri.
     * @return ResponseEntity contenente la lista degli utenti trovati (DTO), o 204 No Content se nessun utente corrisponde.
     * Restituisce 400 Bad Request se l'email è null.
     * @throws SQLException In caso di errori durante l'accesso al database.
     */
    @GetMapping("/developers/search")
    public ResponseEntity<List<UserDTO>> searchDevOrAdminByEmailAndProject(
            @RequestParam String email,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer teamId) throws SQLException {

        if (email == null) {
            return ResponseEntity.badRequest().build();
        }

        List<UserDTO> searchResults;

        if (projectId != null) {

            searchResults = userDAO.searchDevOrAdminByEmailAndProject(email, projectId);

        } else if (teamId != null) {

            searchResults = userDAO.searchDevOrAdminByEmailAndTeam(email, teamId);

        } else {
            searchResults = userDAO.searchDevOrAdminByEmail(email);
        }

        if ( searchResults == null || searchResults.isEmpty()) {
            // If searchResults is null or empty, return 204 No Content
            return ResponseEntity.noContent().build();
        }

        //If there are data, return 200 OK with searchResult
        return ResponseEntity.ok(searchResults);


    }

}
