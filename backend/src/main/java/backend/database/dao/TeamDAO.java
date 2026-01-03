package backend.database.dao;

import backend.dto.TeamDTO;
import backend.dto.TeamReportDTO;

import java.sql.SQLException;
import java.util.List;

public interface TeamDAO {

    List<TeamDTO> searchTeamsByNameAndProject(String teamName, Integer projectId) throws SQLException;

    void createTeam(TeamDTO teamToCreate) throws SQLException;

    boolean addMemberToTeam(Integer teamId, String email) throws SQLException;

    boolean removeMemberFromTeam(Integer teamId, String email) throws SQLException;

    TeamReportDTO generateMonthlyReport(Integer teamId, String month, String year) throws SQLException;

}
