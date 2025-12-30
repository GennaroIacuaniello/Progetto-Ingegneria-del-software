package backend.database.implNEONDB;

import backend.database.DatabaseConnection;
import backend.database.dao.ProjectDAO;
import backend.database.dao.TeamDAO;
import backend.dto.ProjectDTO;
import backend.dto.TeamDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamDAOImpl implements TeamDAO {

    private final DataSource dataSource;

    public TeamDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<TeamDTO> searchTeamsByNameAndProject(String teamName, Integer projectId) throws SQLException{


        List<TeamDTO> searchResult = null;

        String query = "SELECT * FROM Team T WHERE team_name ILIKE ? AND project_id = ?;";

        String toSearch = "%" + teamName + "%";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, toSearch);
            statement.setInt(2, projectId);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            ProjectDTO relatedProject = new ProjectDTO();
            relatedProject.setId(projectId);

            while (rs.next()) {

                TeamDTO foundedTeam = new TeamDTO();

                foundedTeam.setId(rs.getInt("team_id"));
                foundedTeam.setName(rs.getString("team_name"));
                foundedTeam.setProject(relatedProject);

                searchResult.add(foundedTeam);

            }

            rs.close();

        }

        return searchResult;

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
