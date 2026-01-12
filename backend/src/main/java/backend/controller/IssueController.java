package backend.controller;

import backend.database.dao.IssueDAO;

import backend.dto.IssueDTO;
import backend.dto.IssueStatusDTO;

import backend.dto.IssueTypeDTO;
import backend.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

import java.util.List;

/**
 * Controller REST per la gestione delle segnalazioni (Issue).
 * <p>
 * Questa classe gestisce il ciclo di vita delle segnalazioni, esponendo API per
 * la creazione, la ricerca avanzata, la visualizzazione dei dettagli,
 * l'aggiornamento dello stato e l'assegnazione a uno sviluppatore.
 * Tutti gli endpoint sono mappati sotto il percorso base "/issues".
 * </p>
 */
@RestController
@RequestMapping("/issues")
public class IssueController {

    /**
     * Componente di accesso ai dati (DAO) per le operazioni sulle segnalazioni.
     * Gestisce l'interazione diretta con il database per le operazioni CRUD.
     */
    private final IssueDAO issueDAO;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param issueDAO Il DAO per la gestione delle issue.
     */
    public IssueController(IssueDAO issueDAO) {
        this.issueDAO = issueDAO;
    }

    /**
     * Crea e registra una nuova segnalazione nel sistema.
     *
     * @param issueToReport DTO contenente i dati della segnalazione da creare.
     * @return ResponseEntity con un messaggio di conferma in caso di successo.
     * @throws SQLException In caso di errori durante il salvataggio nel database.
     */
    @PostMapping
    public ResponseEntity<String> reportIssue(@RequestBody IssueDTO issueToReport) throws SQLException {

        issueDAO.reportIssue(issueToReport);

        return ResponseEntity.ok("Report success!");

    }

    /**
     * Effettua una ricerca avanzata delle segnalazioni in base a molteplici criteri.
     * <p>
     * Permette di filtrare le issue per titolo, stato, tag, tipo, priorità,
     * e in base agli utenti coinvolti (reporter o risolutore).
     * </p>
     *
     * @param title      (Opzionale) Titolo o parte del titolo da cercare.
     * @param status     (Opzionale) Stato della segnalazione (es. OPEN, CLOSED).
     * @param tags       (Opzionale) Tag associati alla segnalazione.
     * @param type       (Opzionale) Tipologia di segnalazione (es. BUG, FEATURE).
     * @param priority   (Opzionale) Livello di priorità.
     * @param resolverId (Opzionale) ID dello sviluppatore assegnato.
     * @param reporterId (Opzionale) ID dell'utente che ha aperto la segnalazione.
     * @param projectId  ID del progetto (obbligatorio) in cui effettuare la ricerca.
     * @return ResponseEntity contenente la lista delle segnalazioni trovate o 204 No Content se vuota.
     * @throws SQLException In caso di errori durante la lettura dal database.
     */
    @GetMapping("/search")
    public ResponseEntity<List<IssueDTO>> searchIssues(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) Integer resolverId,
            @RequestParam(required = false) Integer reporterId,
            @RequestParam Integer projectId
    ) throws SQLException{

            IssueDTO issueToSearch = new IssueDTO();
            issueToSearch.setTitle(title);
            if(status != null && !status.isEmpty())
                issueToSearch.setStatus(IssueStatusDTO.valueOf(status));
            else
                issueToSearch.setStatus(null);

            issueToSearch.setTags(tags);

            if(type != null && !type.isEmpty())
                issueToSearch.setType(IssueTypeDTO.valueOf(type));
            else
                issueToSearch.setType(null);

            issueToSearch.setPriority(priority);



            List<IssueDTO> searchResults = issueDAO.searchIssues(issueToSearch, resolverId, reporterId, projectId);

            if ( searchResults == null || searchResults.isEmpty()) {
                // If searchResults is null or empty, return 204 No Content
                return ResponseEntity.noContent().build();
            }

            //If there are data, return 200 OK with searchResult
            return ResponseEntity.ok(searchResults);


    }

    /**
     * Recupera i dettagli completi di una specifica segnalazione tramite il suo ID.
     *
     * @param id L'identificativo univoco della segnalazione.
     * @return ResponseEntity con il DTO della segnalazione se trovata, altrimenti 404 Not Found.
     * @throws SQLException In caso di errori di accesso al database.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IssueDTO> getIssueById(@PathVariable("id") int id) throws SQLException{

        IssueDTO issue = issueDAO.getIssueById(id);

        if (issue != null) {

            return ResponseEntity.ok(issue);

        } else {

            return ResponseEntity.notFound().build();

        }

    }

    /**
     * Aggiorna lo stato di avanzamento di una segnalazione.
     *
     * @param id      L'identificativo della segnalazione da aggiornare.
     * @param request Oggetto contenente il nuovo stato da impostare.
     * @return ResponseEntity con messaggio di successo o errore se lo stato è mancante o l' issue non esiste.
     * @throws SQLException In caso di errori durante l'aggiornamento nel database.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateIssueStatus(
            @PathVariable("id") int id,
            @RequestBody StatusUpdateRequest request) throws SQLException{

        IssueStatusDTO newStatus = request.getNewStatus();

        if (newStatus == null || newStatus.toString().isEmpty()) {
            return ResponseEntity.badRequest().body("Status error: missing");
        }


        boolean updated = issueDAO.updateStatus(id, newStatus);

        if (updated) {
            return ResponseEntity.ok("Status update success");
        } else {
            return ResponseEntity.status(404).body("Issue not found");
        }

    }

    /**
     * DTO utilizzato per incapsulare la richiesta di aggiornamento dello stato.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {

        private IssueStatusDTO newStatus;

    }

    /**
     * Assegna una segnalazione a uno specifico sviluppatore (resolver).
     *
     * @param id       L'identificativo della segnalazione da assegnare.
     * @param resolver Oggetto UserDTO contenente l'email dello sviluppatore.
     * @return ResponseEntity con i dati dello sviluppatore assegnato.
     * @throws SQLException            In caso di errori nel database.
     * @throws ResponseStatusException 400 Bad Request se l'email manca, 404 Not Found se utente o issue non esistono.
     */
    @PutMapping("/{id}/resolver")
    public ResponseEntity<UserDTO> assignIssueToDeveloper(
            @PathVariable("id") int id,
            @RequestBody UserDTO resolver) throws SQLException{

        String resolverEmail = resolver.getEmail();

        if (resolverEmail == null || resolverEmail.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assigning error: missing email");
        }

        UserDTO assignedUser = issueDAO.assignIssueToDeveloperByEmail(id, resolverEmail);

        if (assignedUser != null) {
            return ResponseEntity.ok(assignedUser);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with specified email not found or issue does not exist");
        }

    }


}
