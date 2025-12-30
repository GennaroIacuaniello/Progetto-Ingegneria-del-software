package backend.database.implNEONDB;

import backend.database.DatabaseConnection;
import backend.database.dao.TeamDAO;
import backend.dto.TeamDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamDAOImpl implements TeamDAO {

    private final DataSource dataSource;

    public TeamDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<TeamDTO> searchTeamsByName(String teamName) throws SQLException{

        return new ArrayList<>();

    }

    public void createTeam(TeamDTO teamToCreate) throws SQLException{

        String query = "INSERT INTO Team (team_name, project_id) VALUES "+
                       "(?, ?);";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, teamToCreate.getName());
            statement.setInt(2, teamToCreate.getProject().getId());

            statement.executeUpdate();

        }

    }


}
