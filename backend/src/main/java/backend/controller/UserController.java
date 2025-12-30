package backend.controller;


import backend.database.dao.UserDAO;
import backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
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

        List<UserDTO> searchResults = null;

        if (projectId != null) {

            searchResults = userDAO.searchDevOrAdminByEmailAndProject(email, projectId);

        } else if (teamId != null) {

            searchResults = userDAO.searchDevOrAdminByEmailAndTeam(email, teamId);

        } else {
            searchResults = userDAO.searchDevOrAdminByEmail(email);
        }

        if ( searchResults == null || searchResults.isEmpty()) {
            // Se la lista è vuota, restituisce un 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(searchResults);


    }


}
