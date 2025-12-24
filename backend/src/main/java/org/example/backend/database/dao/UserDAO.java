package backend.database.dao;

import backend.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    void searchUserByMail(String email, List<UserDTO> users) throws SQLException;

}
