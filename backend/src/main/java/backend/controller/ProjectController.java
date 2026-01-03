package backend.controller;

import backend.database.dao.ProjectDAO;
import backend.dto.ProjectDTO;
import backend.dto.StatisticDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectDAO projectDAO;

    public ProjectController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectDTO>> searchProjects(@RequestParam("name") String projectName) throws SQLException {

        List<ProjectDTO> searchResults = projectDAO.searchProjectsByName(projectName);

        if ( searchResults == null || searchResults.isEmpty()) {
            // Se la lista è vuota, restituisce un 204 No Content
            return ResponseEntity.noContent().build();
        }

        //Se ci sono dati, restituisce 200 OK con il corpo (la lista)
        return ResponseEntity.ok(searchResults);
    }

    @PostMapping
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectToCreate) throws SQLException {

        projectDAO.createProject(projectToCreate);

        return ResponseEntity.ok("Project created successfully!");

    }

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
