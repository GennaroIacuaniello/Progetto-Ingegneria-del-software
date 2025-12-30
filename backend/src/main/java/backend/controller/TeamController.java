package backend.controller;


import backend.database.dao.TeamDAO;
import backend.dto.TeamDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamDAO teamDAO;

    public TeamController(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody TeamDTO teamToCreate) throws SQLException {

        teamDAO.createTeam(teamToCreate);

        return ResponseEntity.ok("Team created successfully!");

    }


}
