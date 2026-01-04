package backend.database.implneondb;


import backend.database.dao.ProjectDAO;
import backend.dto.IssueDTO;
import backend.dto.ProjectDTO;
import backend.dto.StatisticDTO;
import backend.dto.UserDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

    private final DataSource dataSource;

    public ProjectDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ProjectDTO> searchProjectsByName(String projectName) throws SQLException{


        List<ProjectDTO> searchResult;

        String query = "SELECT P.* FROM Project P WHERE project_name ILIKE ?;";

        String toSearch = "%" + projectName + "%";

        try (Connection connection = dataSource.getConnection();
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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projectToCreate.getName());

            statement.executeUpdate();

        }

    }

    public StatisticDTO generateDashboard() throws SQLException{

        StatisticDTO dashboardData;

        String query = "SELECT DISTINCT I.issue_id, I.resolver_id, I.report_time, I.resolution_time, U1.email AS resolver_email " +
                       "FROM Issue I LEFT JOIN User_ U1 ON  I.resolver_id = U1.user_id ";

        ArrayList<Integer> numIssueSolvedForDev = new ArrayList<>();
        ArrayList<Integer> devAlreadyFoundedIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();

            dashboardData = new StatisticDTO();

            Duration totalDuration = Duration.ZERO;

            int resolvedCount = 0;

            while (rs.next()) {

                IssueDTO foundedIssue = new IssueDTO();

                foundedIssue.setId(rs.getInt("issue_id"));


                int resolverId = rs.getInt("resolver_id");

                //rs.wasNull() checks if last column was NULL
                if (!rs.wasNull() && resolverId >= 0) {

                    resolverHandler(devAlreadyFoundedIds, resolverId, dashboardData, foundedIssue, rs, numIssueSolvedForDev);


                } else {

                    foundedIssue.setAssignedDeveloper(null);
                    Integer prevNumIssuesNotAssigned = dashboardData.getNumIssuesNotAssigned();

                    dashboardData.setNumIssuesNotAssigned(prevNumIssuesNotAssigned + 1);

                }


                Timestamp reportTimestamp = rs.getTimestamp("report_time");
                Timestamp foundedResolutionTimestamp = rs.getTimestamp("resolution_time");

                foundedIssue.setReportDate(new java.util.Date(reportTimestamp.getTime()));

                if (foundedResolutionTimestamp != null) {

                    foundedIssue.setResolutionDate(new Date(foundedResolutionTimestamp.getTime()));

                    dashboardData.getClosedIssues().add(foundedIssue);

                    Duration issueDuration = Duration.between(reportTimestamp.toInstant(), foundedResolutionTimestamp.toInstant());
                    totalDuration = totalDuration.plus(issueDuration);
                    resolvedCount++;

                    manageDevAverageDuration(dashboardData, foundedIssue, issueDuration, numIssueSolvedForDev);

                }else{

                    manageDevNumOpenIssues(dashboardData, foundedIssue);

                }


            }

            countingAndAverageCalculation(dashboardData, numIssueSolvedForDev, resolvedCount, totalDuration);


            rs.close();

        }


        return dashboardData;


    }

    public static void resolverHandler(List<Integer> devAlreadyFoundedIds, int resolverId, StatisticDTO dashboardData, IssueDTO foundedIssue, ResultSet rs, List<Integer> numIssueSolvedForDev) throws SQLException {

        if(devAlreadyFoundedIds.contains(resolverId)){
            for(UserDTO dev: dashboardData.getDevelopers())
                if(dev.getId() == resolverId){
                    foundedIssue.setAssignedDeveloper(dev);
                    break;
                }
        } else {

            UserDTO resolver = new UserDTO();
            resolver.setId(resolverId);
            resolver.setEmail(rs.getString("resolver_email"));

            foundedIssue.setAssignedDeveloper(resolver);
            devAlreadyFoundedIds.add(resolverId);


            dashboardData.getDevelopers().add(resolver);

            dashboardData.getAverageResolutionDurations().add(Duration.ZERO);

            dashboardData.getNumClosedIssues().add(0);
            dashboardData.getNumOpenIssues().add(0);


            numIssueSolvedForDev.add(0);
        }
    }

    public static void manageDevAverageDuration(StatisticDTO dashboardData, IssueDTO foundedIssue, Duration issueDuration, List<Integer> numIssueSolvedForDev) {
        int devIndex = dashboardData.getDevelopers().indexOf(foundedIssue.getAssignedDeveloper());

        if (devIndex != -1) {

            Duration currentSum = dashboardData.getAverageResolutionDurations().get(devIndex);

            dashboardData.getAverageResolutionDurations().set(devIndex, currentSum.plus(issueDuration));

            numIssueSolvedForDev.set(devIndex, numIssueSolvedForDev.get(devIndex) + 1);

        }
    }

    public static void manageDevNumOpenIssues(StatisticDTO dashboardData, IssueDTO foundedIssue) {
        int devIndex = dashboardData.getDevelopers().indexOf(foundedIssue.getAssignedDeveloper());

        if (devIndex != -1) {

            Integer prevNumOpenIssues = dashboardData.getNumOpenIssues().get(devIndex);

            dashboardData.getNumOpenIssues().set(devIndex, prevNumOpenIssues + 1);

        }

        foundedIssue.setResolutionDate(null);
        dashboardData.getOpenIssues().add(foundedIssue);
    }

    public static void countingAndAverageCalculation(StatisticDTO dashboardData, List<Integer> numIssueSolvedForDev, int resolvedCount, Duration totalDuration) {
        for (int i = 0; i < dashboardData.getAverageResolutionDurations().size(); i++) {

            int count = numIssueSolvedForDev.get(i);

            dashboardData.getNumClosedIssues().set(i, count);

            if (count > 0) {
                Duration totalSum = dashboardData.getAverageResolutionDurations().get(i);
                dashboardData.getAverageResolutionDurations().set(i, totalSum.dividedBy(count));
            }

        }

        if (resolvedCount > 0) {

            Duration averageDuration = totalDuration.dividedBy(resolvedCount);

            dashboardData.setTotalAverageResolutionDuration(averageDuration);
        }
    }

}
