package backend.database.implneondb;

import backend.database.dao.UserDAO;
import backend.dto.UserDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final DataSource dataSource;

    private static final String USER_ID = "user_id";
    private static final String EMAIL = "email";

    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public UserDTO searchUserByMail(String email) throws SQLException{

        UserDTO foundedUser = null;

        String query = "SELECT U.* FROM User_ U WHERE email LIKE ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                foundedUser = new UserDTO();

                foundedUser.setId(rs.getInt(USER_ID));
                foundedUser.setEmail(rs.getString(EMAIL));
                foundedUser.setPassword(rs.getString("hashed_password"));
                foundedUser.setRole(rs.getInt("user_type"));

            }

            rs.close();

        }

        return foundedUser;
    }

    public void registerNewUser(UserDTO newUser) throws SQLException{

        String query = "INSERT INTO User_ (email, hashed_password, user_type) VALUES "+
                "(?, ?, ?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newUser.getEmail());
            statement.setString(2, newUser.getPassword());
            statement.setInt(3, newUser.getRole());

            statement.executeUpdate();


        }

    }

    public List<UserDTO> searchDevOrAdminByEmailAndProject(String email, Integer projectId) throws SQLException{

        List<UserDTO> searchResult;

        String query = "SELECT * FROM User_ U NATURAL JOIN Works_on W " +
                       "WHERE U.email ILIKE ? AND U.user_type > 0 AND W.project_id = ?;";

        searchResult = performSearch(email, projectId, query);

        return searchResult;


    }

    public List<UserDTO> searchDevOrAdminByEmailAndTeam(String email, Integer teamId) throws SQLException{

        List<UserDTO> searchResult;

        String query = "SELECT * FROM User_ U NATURAL JOIN Works_in W " +
                "WHERE U.email ILIKE ? AND W.team_id = ?;";

        searchResult = performSearch(email, teamId, query);

        return searchResult;


    }

    private List<UserDTO> performSearch(String email, Integer objectId, String query) throws SQLException {

        List<UserDTO> searchResult;
        String emailToSearch = "%" + email + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, emailToSearch);
            statement.setInt(2, objectId);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                UserDTO foundedUser = new UserDTO();

                foundedUser.setId(rs.getInt(USER_ID));
                foundedUser.setEmail(rs.getString(EMAIL) );

                searchResult.add(foundedUser);


            }

            rs.close();

        }
        return searchResult;
    }

    public List<UserDTO> searchDevOrAdminByEmail(String email) throws SQLException{

        List<UserDTO> searchResult;

        String query = "SELECT * FROM User_ U " +
                       "WHERE U.email ILIKE ? AND U.user_type > 0;";

        String emailToSearch = "%" + email + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, emailToSearch);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                UserDTO foundedUser = new UserDTO();

                foundedUser.setId(rs.getInt(USER_ID));
                foundedUser.setEmail(rs.getString(EMAIL) );

                searchResult.add(foundedUser);

            }

            rs.close();

        }

        return searchResult;

    }


}
