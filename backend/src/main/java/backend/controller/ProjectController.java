package backend.controller;

import backend.database.dao.ProjectDAO;
import backend.database.dao.UserDAO;
import backend.dto.ProjectDTO;
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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectDAO projectDAO;

    public ProjectController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> searchProjects(@RequestParam("search") String projectName) throws SQLException {

        List<ProjectDTO> searchResults = projectDAO.searchProjectsByName(projectName);

        if ( searchResults == null || searchResults.isEmpty()) {
            // Se la lista è vuota, restituisce un 204 No Content
            return ResponseEntity.noContent().build();
        }

        //Se ci sono dati, restituisce 200 OK con il corpo (la lista)
        return ResponseEntity.ok(searchResults);
    }


}
