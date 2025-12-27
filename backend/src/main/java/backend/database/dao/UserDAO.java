package backend.database.dao;

import backend.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    void resisterNewUser(UserDTO newUser) throws SQLException;

    UserDTO searchUserByMail(String email) throws SQLException;

}
