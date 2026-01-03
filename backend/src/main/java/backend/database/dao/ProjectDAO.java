package backend.database.dao;

import backend.dto.ProjectDTO;
import backend.dto.StatisticDTO;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {

    List<ProjectDTO> searchProjectsByName(String projectName) throws SQLException;

    void createProject(ProjectDTO projectToCreate) throws SQLException;

    StatisticDTO generateDashboard() throws SQLException;

}
