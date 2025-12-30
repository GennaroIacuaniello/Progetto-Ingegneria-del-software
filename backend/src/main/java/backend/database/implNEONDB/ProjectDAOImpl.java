package backend.database.implNEONDB;

import backend.database.DatabaseConnection;
import backend.database.dao.ProjectDAO;
import backend.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

    private final DataSource dataSource;

    public ProjectDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ProjectDTO> searchProjectsByName(String projectName) throws SQLException{


        List<ProjectDTO> searchResult = null;

        String query = "SELECT * FROM Project P WHERE project_name ILIKE ?;";

        String toSearch = "%" + projectName + "%";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, toSearch);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                searchResult.add(new ProjectDTO( rs.getInt("project_id"), rs.getString("project_name") ));


            }

            rs.close();

        }

        return searchResult;

    }

    public void createProject(ProjectDTO projectToCreate) throws SQLException{

        String query = "INSERT INTO Project (project_name) VALUES "+
                       "(?);";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projectToCreate.getName());

            statement.executeUpdate();

        }

    }


}
