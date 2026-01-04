package backend.controller;


import backend.database.dao.UserDAO;
import backend.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDAO userDAO;


    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/developers/search")
    public ResponseEntity<List<UserDTO>> searchDevOrAdminByEmailAndProject(
            @RequestParam String email,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer teamId) throws SQLException {

        if (email == null) {
            return ResponseEntity.badRequest().build();
        }

        List<UserDTO> searchResults;

        if (projectId != null) {

            searchResults = userDAO.searchDevOrAdminByEmailAndProject(email, projectId);

        } else if (teamId != null) {

            searchResults = userDAO.searchDevOrAdminByEmailAndTeam(email, teamId);

        } else {
            searchResults = userDAO.searchDevOrAdminByEmail(email);
        }

        if ( searchResults == null || searchResults.isEmpty()) {
            // If searchResults is null or empty, return 204 No Content
            return ResponseEntity.noContent().build();
        }

        //If there are data, return 200 OK with searchResult
        return ResponseEntity.ok(searchResults);


    }


}
