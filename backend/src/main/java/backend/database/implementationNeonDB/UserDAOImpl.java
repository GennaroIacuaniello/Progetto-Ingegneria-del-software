package backend.database.implementationNeonDB;

import backend.database.dao.UserDAO;
import backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private DataSource dataSource;

    public List<UserDTO> searchUserByMail(String email) throws SQLException{
        return new ArrayList<>();
    }

}
