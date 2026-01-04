package backend.database.implneondb;

import backend.database.dao.TeamDAO;
import backend.dto.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
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

        try (Connection connection = dataSource.getConnection();
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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, teamToCreate.getName());
            statement.setInt(2, teamToCreate.getProject().getId());

            statement.executeUpdate();

        }

    }

    public boolean addMemberToTeam(Integer teamId, String email) throws SQLException{

        String query = "INSERT INTO Works_in (team_id, user_id) VALUES "+
                       "(?, (SELECT user_id FROM User_ U WHERE U.email = ?))";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, teamId);
            statement.setString(2, email);

            int rowsAdded = statement.executeUpdate();

            return rowsAdded > 0;
        }

    }



    public boolean removeMemberFromTeam(Integer teamId, String email) throws SQLException{

        String query = "DELETE FROM Works_in " +
                       "WHERE team_id = ? " +
                       "AND user_id = (SELECT user_id FROM User_ WHERE email = ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, teamId);
            statement.setString(2, email);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;
        }

    }

    public StatisticDTO generateMonthlyReport(Integer teamId, String month, String year) throws SQLException{

        StatisticDTO reportGenerated = null;

        String query = "SELECT DISTINCT I.issue_id, I.resolver_id, I.report_time, I.resolution_time, U1.email AS resolver_email " +
                       "FROM Team T " +
                       "JOIN Project P ON T.project_id = P.project_id " +
                       "JOIN Works_in W ON T.team_id = W.team_id " +
                       "JOIN User_ U1 ON W.user_id = U1.user_id " +
                       "JOIN Issue I ON (I.project_id = P.project_id AND I.resolver_id = U1.user_id) " +
                       "WHERE T.team_id = ? " +
                       "AND ( " +
                       "  (EXTRACT(MONTH FROM I.report_time) = ? AND EXTRACT(YEAR FROM I.report_time) = ?) " +
                       "  OR " +
                       "  (EXTRACT(MONTH FROM I.resolution_time) = ? AND EXTRACT(YEAR FROM I.resolution_time) = ?) " +
                       ")";

        ArrayList<Integer> numIssueSolvedForDev = new ArrayList<>();
        ArrayList<Integer> durationsDevIds = new ArrayList<>();
        ArrayList<Integer> devAlreadyFoundedIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, teamId);
            statement.setInt(2, stringMonthToDBInt(month));
            statement.setInt(3, Integer.parseInt(year));
            statement.setInt(4, stringMonthToDBInt(month));
            statement.setInt(5, Integer.parseInt(year));


            ResultSet rs = statement.executeQuery();

            reportGenerated = new StatisticDTO();

            Duration totalDuration = Duration.ZERO;

            int resolvedCount = 0;

            while (rs.next()) {

                IssueDTO foundedIssue = new IssueDTO();

                foundedIssue.setId(rs.getInt("issue_id"));


                int resolverId = rs.getInt("resolver_id");

                //rs.wasNull() checks if last column was NULL
                if (!rs.wasNull() && resolverId >= 0) {

                    ProjectDAOImpl.resolverHandler(devAlreadyFoundedIds, resolverId, reportGenerated, foundedIssue, rs, numIssueSolvedForDev);


                } else {

                    foundedIssue.setAssignedDeveloper(null);

                }


                Timestamp reportTimestamp = rs.getTimestamp("report_time");
                Timestamp resolutionTimestamp = rs.getTimestamp("resolution_time");

                foundedIssue.setReportDate(new Date(reportTimestamp.getTime()));

                if (resolutionTimestamp != null) {

                    foundedIssue.setResolutionDate(new Date(resolutionTimestamp.getTime()));

                    Duration issueDuration = Duration.between(reportTimestamp.toInstant(), resolutionTimestamp.toInstant());
                    totalDuration = totalDuration.plus(issueDuration);
                    resolvedCount++;

                    reportGenerated.getClosedIssues().add(foundedIssue);

                    int devIndex = reportGenerated.getDevelopers().indexOf(foundedIssue.getAssignedDeveloper());

                    if (devIndex != -1) {

                        Duration currentSum = reportGenerated.getAverageResolutionDurations().get(devIndex);

                        reportGenerated.getAverageResolutionDurations().set(devIndex, currentSum.plus(issueDuration));

                        numIssueSolvedForDev.set(devIndex, numIssueSolvedForDev.get(devIndex) + 1);

                    }

                }else{

                    int devIndex = reportGenerated.getDevelopers().indexOf(foundedIssue.getAssignedDeveloper());

                    if (devIndex != -1) {

                        Integer prevNumOpenIssues = reportGenerated.getNumOpenIssues().get(devIndex);

                        reportGenerated.getNumOpenIssues().set(devIndex, prevNumOpenIssues + 1);

                    }

                    foundedIssue.setResolutionDate(null);
                    reportGenerated.getOpenIssues().add(foundedIssue);

                }


            }

            for (int i = 0; i < reportGenerated.getAverageResolutionDurations().size(); i++) {

                int count = numIssueSolvedForDev.get(i);

                reportGenerated.getNumClosedIssues().set(i, count);

                if (count > 0) {
                    Duration totalSum = reportGenerated.getAverageResolutionDurations().get(i);
                    reportGenerated.getAverageResolutionDurations().set(i, totalSum.dividedBy(count));
                }

            }

            if (resolvedCount > 0) {

                Duration averageDuration = totalDuration.dividedBy(resolvedCount);

                reportGenerated.setTotalAverageResolutionDuration(averageDuration);
            }


            rs.close();

        }


        return reportGenerated;

    }

    private int stringMonthToDBInt(String monthName) {

        return switch(monthName.toLowerCase()) {
            case "gennaio" -> 1;
            case "febbraio" -> 2;
            case "marzo" -> 3;
            case "aprile" -> 4;
            case "maggio" -> 5;
            case "giugno" -> 6;
            case "luglio" -> 7;
            case "agosto" -> 8;
            case "settembre" -> 9;
            case "ottobre" -> 10;
            case "novembre" -> 11;
            case "dicembre" -> 12;
            default -> 0;
        };
    }

}
