package backend.controller;

import backend.database.dao.TeamDAO;
import backend.dto.TeamDTO;
import backend.dto.StatisticDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller REST per la gestione dei team di progetto.
 * <p>
 * Questa classe espone gli endpoint per l'amministrazione dei team, permettendo di:
 * creare nuovi gruppi di lavoro, aggiungere o rimuovere membri, ricercare team
 * e generare report statistici mensili sulle performance del gruppo.
 * Tutti gli endpoint sono mappati sotto il percorso base "/teams".
 * </p>
 */
@RestController
@RequestMapping("/teams")
public class TeamController {

    /**
     * Componente di accesso ai dati (DAO) per le operazioni sui team.
     * Gestisce l'interazione con il database per la gestione dei membri,
     * la creazione dei gruppi e la generazione dei report.
     */
    private final TeamDAO teamDAO;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param teamDAO Il DAO per la gestione dei team.
     */
    public TeamController(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }

    /**
     * Cerca i team all'interno di un progetto specifico filtrando per nome.
     *
     * @param teamName  Il nome (o parte del nome) del team da cercare.
     * @param projectId L'identificativo del progetto a cui i team devono appartenere.
     * @return ResponseEntity contenente la lista dei team trovati o 204 No Content se nessun team corrisponde ai criteri.
     * @throws SQLException In caso di errori durante la ricerca nel database.
     */
    @GetMapping("/search")
    public ResponseEntity<List<TeamDTO>> searchTeams(
            @RequestParam String teamName,
            @RequestParam Integer projectId
    ) throws SQLException {

        List<TeamDTO> searchResults = teamDAO.searchTeamsByNameAndProject(teamName, projectId);

        if ( searchResults == null || searchResults.isEmpty()) {
            // If searchResults is null or empty, return 204 No Content
            return ResponseEntity.noContent().build();
        }

        //If there are data, return 200 OK with searchResult
        return ResponseEntity.ok(searchResults);
    }

    /**
     * Crea un nuovo team nel sistema.
     *
     * @param teamToCreate DTO contenente i dati del nuovo team (es nome, progetto di appartenenza).
     * @return ResponseEntity con un messaggio di conferma della creazione.
     * @throws SQLException In caso di errori durante il salvataggio nel database.
     */
    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody TeamDTO teamToCreate) throws SQLException {

        teamDAO.createTeam(teamToCreate);

        return ResponseEntity.ok("Team created successfully!");

    }

    /**
     * Aggiunge un membro a un team esistente.
     * <p>
     * Associa un utente (identificato tramite email) a uno specifico team.
     * </p>
     *
     * @param teamId L'identificativo del team a cui aggiungere il membro.
     * @param email  L'indirizzo email dell'utente da aggiungere.
     * @return ResponseEntity con messaggio di successo, oppure 409 Conflict se l'utente è già presente o non trovato.
     * @throws SQLException In caso di errori durante l'aggiornamento nel database.
     */
    @PostMapping("/add-member")
    public ResponseEntity<String> addMember(
            @RequestParam int teamId,
            @RequestParam String email) throws SQLException {

        boolean added = teamDAO.addMemberToTeam(teamId, email);

        if (added) {
            return ResponseEntity.ok("Member added successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already in team or user not found!");
        }
    }

    /**
     * Rimuove un membro da un team.
     *
     * @param teamId L'identificativo del team da cui rimuovere il membro.
     * @param email  L'indirizzo email dell'utente da rimuovere.
     * @return ResponseEntity con messaggio di successo, oppure 404 Not Found se il team o l'utente non esistono.
     * @throws SQLException In caso di errori durante l'operazione nel database.
     */
    @DeleteMapping("/remove-member")
    public ResponseEntity<String> removeMember(
            @RequestParam int teamId,
            @RequestParam String email) throws SQLException {

        boolean removed = teamDAO.removeMemberFromTeam(teamId, email);

        if (removed) {
            return ResponseEntity.ok("Member deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Team not found!");
        }
    }

    /**
     * Genera un report statistico mensile per un determinato team.
     * <p>
     * Calcola statistiche sulle attività del team (es issue risolte) per il mese e l'anno specificati.
     * </p>
     *
     * @param teamId L'identificativo del team di cui generare il report.
     * @param month  Il mese di riferimento per il report.
     * @param year   L'anno di riferimento per il report.
     * @return ResponseEntity contenente il DTO con le statistiche, o 404 Not Found se i dati non sono disponibili.
     * @throws SQLException In caso di errori nel recupero dei dati dal database.
     */
    @GetMapping("/{teamId}/report")
    public ResponseEntity<StatisticDTO> getTeamReport(
            @PathVariable Integer teamId,
            @RequestParam String month,
            @RequestParam String year) throws SQLException {


        StatisticDTO report = teamDAO.generateMonthlyReport(teamId, month, year);

        if (report != null) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
