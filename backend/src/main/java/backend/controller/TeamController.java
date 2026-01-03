package backend.controller;


import backend.database.dao.TeamDAO;
import backend.dto.TeamDTO;
import backend.dto.StatisticDTO;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/add-member")
    public ResponseEntity<String> addMember(
            @RequestParam int teamId,
            @RequestParam String email) throws SQLException {

        boolean added = teamDAO.addMemberToTeam(teamId, email);

        if (added) {
            return ResponseEntity.ok("Member addedd successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already in team or user not found!");
        }
    }

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
