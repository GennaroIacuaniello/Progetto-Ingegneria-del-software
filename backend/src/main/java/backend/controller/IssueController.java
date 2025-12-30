package backend.controller;

import backend.database.dao.IssueDAO;
import backend.database.dao.ProjectDAO;
import backend.database.dao.UserDAO;
import backend.dto.IssueDTO;
import backend.dto.IssueStatusDTO;
import backend.dto.ProjectDTO;
import backend.dto.UserDTO;
import backend.model.Project;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<String> reportIssue(@RequestBody IssueDTO issueToReport) throws SQLException {

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
            @RequestParam(required = false) Integer reporterId,
            @RequestParam Integer projectId
    ) throws SQLException{

            List<IssueDTO> searchResults = issueDAO.searchIssues(title, status, tags, type, priority, resolverId, reporterId, projectId);

            if ( searchResults == null || searchResults.isEmpty()) {
                // Se la lista è vuota, restituisce un 204 No Content
                return ResponseEntity.noContent().build();
            }

            //Se ci sono dati, restituisce 200 OK con il corpo (la lista)
            return ResponseEntity.ok(searchResults);


    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueDTO> getIssueById(@PathVariable("id") int id) throws SQLException{

        IssueDTO issue = issueDAO.getIssueById(id);

        if (issue != null) {

            return ResponseEntity.ok(issue);

        } else {

            return ResponseEntity.notFound().build();

        }

    }

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

    public static class StatusUpdateRequest {

        private IssueStatusDTO newStatus;

        public StatusUpdateRequest() {
            //Empty constructor needed for jackson
        }

        public StatusUpdateRequest(IssueStatusDTO newStatus) {
            this.newStatus = newStatus;
        }

        public IssueStatusDTO getNewStatus() {
            return newStatus;
        }

        public void setNewStatus(IssueStatusDTO newStatus) {
            this.newStatus = newStatus;
        }
    }

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
