package backend.database.dao;

import backend.dto.ProjectDTO;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {

    List<ProjectDTO> searchProjectsByName(String projectName) throws SQLException;

}
