package backend.controller;

import backend.database.dao.ProjectDAO;
import backend.dto.ProjectDTO;
import backend.dto.StatisticDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller REST per la gestione dei progetti.
 * <p>
 * Questa classe gestisce le operazioni relative ai progetti, offrendo API per
 * la ricerca tramite nome, la creazione di nuovi progetti e la generazione
 * delle statistiche globali per la dashboard.
 * Tutti gli endpoint sono mappati sotto il percorso base "/projects".
 * </p>
 */
@RestController
@RequestMapping("/projects")
public class ProjectController {

    /**
     * Componente di accesso ai dati (DAO) per le operazioni sui progetti.
     * Gestisce l'interazione con il database per creare progetti, effettuare ricerche
     * e aggregare i dati statistici per la dashboard.
     */
    private final ProjectDAO projectDAO;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param projectDAO Il DAO per la gestione dei progetti.
     */
    public ProjectController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    /**
     * Cerca i progetti che corrispondono a un determinato nome.
     * <p>
     * Esegue una ricerca nel database restituendo tutti i progetti il cui nome
     * contiene la stringa specificata.
     * </p>
     *
     * @param projectName Il nome (o parte di esso) del progetto da cercare.
     * @return ResponseEntity contenente la lista dei progetti trovati o 204 No Content se la ricerca non produce risultati.
     * @throws SQLException In caso di errori durante l'accesso al database.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProjectDTO>> searchProjects(@RequestParam("name") String projectName) throws SQLException {

        List<ProjectDTO> searchResults = projectDAO.searchProjectsByName(projectName);

        if ( searchResults == null || searchResults.isEmpty()) {
            // If searchResults is null or empty, return 204 No Content
            return ResponseEntity.noContent().build();
        }

        //If there are data, return 200 OK with searchResult
        return ResponseEntity.ok(searchResults);
    }

    /**
     * Crea un nuovo progetto nel sistema.
     * <p>
     * Riceve i dettagli del progetto tramite il corpo della richiesta e lo persiste nel database.
     * </p>
     *
     * @param projectToCreate DTO contenente i dati del progetto da creare.
     * @return ResponseEntity con un messaggio di conferma dell'avvenuta creazione.
     * @throws SQLException In caso di errori durante il salvataggio nel database.
     */
    @PostMapping
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectToCreate) throws SQLException {

        projectDAO.createProject(projectToCreate);

        return ResponseEntity.ok("Project created successfully!");

    }

    /**
     * Genera e recupera i dati statistici per la dashboard.
     * <p>
     * Restituisce un oggetto aggregato contenente statistiche generali del sistema
     * (come conteggi di progetti, issue, ecc) utili per la visualizzazione nella home page.
     * </p>
     *
     * @return ResponseEntity contenente il DTO delle statistiche o 404 Not Found se i dati non sono disponibili.
     * @throws SQLException In caso di errori nel recupero dei dati dal database.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<StatisticDTO> generateDashboard( ) throws SQLException {


        StatisticDTO dashboardData = projectDAO.generateDashboard();

        if (dashboardData != null) {
            return ResponseEntity.ok(dashboardData);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
