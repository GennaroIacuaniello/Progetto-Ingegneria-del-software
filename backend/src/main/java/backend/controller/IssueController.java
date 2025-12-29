package backend.controller;

import backend.database.dao.IssueDAO;
import backend.database.dao.ProjectDAO;
import backend.database.dao.UserDAO;
import backend.dto.IssueDTO;
import backend.dto.ProjectDTO;
import backend.dto.UserDTO;
import backend.model.Project;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/issues")
public class IssueController {

    private final IssueDAO issueDAO;

    public IssueController(IssueDAO issueDAO) {
        this.issueDAO = issueDAO;
    }

    @PostMapping
    public ResponseEntity<?> reportIssue(@RequestBody IssueDTO issueToReport) throws SQLException {

        issueDAO.reportIssue(issueToReport);

        return ResponseEntity.ok("Report success!");

    }

    @GetMapping("/search")
    public ResponseEntity<List<IssueDTO>> searchIssues(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) Integer resolverId,
            @RequestParam Integer projectId
    ) throws SQLException{

            List<IssueDTO> searchResults = issueDAO.searchIssues(title, status, tags, type, priority, resolverId, projectId);

            if ( searchResults == null || searchResults.isEmpty()) {
                // Se la lista è vuota, restituisce un 204 No Content
                return ResponseEntity.noContent().build();
            }

            //Se ci sono dati, restituisce 200 OK con il corpo (la lista)
            return ResponseEntity.ok(searchResults);


    }


}
