package backend.database.implementationNeonDB;

import backend.database.DatabaseConnection;
import backend.database.dao.ProjectDAO;
import backend.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

    @Autowired
    private DataSource dataSource;

    public List<ProjectDTO> searchProjectsByName(String projectName) throws SQLException{


        List<ProjectDTO> searchResult = null;

        String query = "SELECT * FROM Project P WHERE project_name ILIKE ?";

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


}
