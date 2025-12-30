package backend.database.dao;

import backend.dto.TeamDTO;

import java.sql.SQLException;
import java.util.List;

public interface TeamDAO {

    List<TeamDTO> searchTeamsByName(String teamName) throws SQLException;

    void createTeam(TeamDTO teamToCreate) throws SQLException;

}
