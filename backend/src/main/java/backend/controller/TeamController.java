package backend.controller;


import backend.database.dao.TeamDAO;
import backend.dto.ProjectDTO;
import backend.dto.TeamDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamDAO teamDAO;

    public TeamController(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamDTO>> searchTeams(
            @RequestParam String teamName,
            @RequestParam Integer projectId
    ) throws SQLException {

        List<TeamDTO> searchResults = teamDAO.searchTeamsByNameAndProject(teamName, projectId);

        if ( searchResults == null || searchResults.isEmpty()) {
            // Se la lista è vuota, restituisce un 204 No Content
            return ResponseEntity.noContent().build();
        }

        //Se ci sono dati, restituisce 200 OK con il corpo (la lista)
        return ResponseEntity.ok(searchResults);
    }

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody TeamDTO teamToCreate) throws SQLException {

        teamDAO.createTeam(teamToCreate);

        return ResponseEntity.ok("Team created successfully!");

    }


}
