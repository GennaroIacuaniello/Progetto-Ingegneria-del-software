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


}
