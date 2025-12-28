package backend.database.implNEONDB;

import backend.database.DatabaseConnection;
import backend.database.dao.UserDAO;
import backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private DataSource dataSource;

    public UserDTO searchUserByMail(String email) throws SQLException{

        UserDTO foundedUser = null;

        String query = "SELECT * FROM User_ U WHERE email LIKE ?;";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                foundedUser = new UserDTO();

                foundedUser.setId(rs.getInt("user_id"));
                foundedUser.setEmail(rs.getString("email"));
                foundedUser.setPassword(rs.getString("hashed_password"));
                foundedUser.setRole(rs.getInt("user_type"));

            }

            rs.close();

        }

        return foundedUser;
    }

    public void resisterNewUser(UserDTO newUser) throws SQLException{

        String query = "INSERT INTO User_ (email, hashed_password, user_type) VALUES "+
                "(?, ?, ?);";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newUser.getEmail());
            statement.setString(2, newUser.getPassword());
            statement.setInt(3, newUser.getRole());

            statement.executeUpdate();


        }

    }

}
