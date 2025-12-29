package backend.database.implNEONDB;

import backend.database.DatabaseConnection;
import backend.database.dao.IssueDAO;
import backend.dto.IssueDTO;
import backend.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class IssueDAOImpl implements IssueDAO {

    @Autowired
    private DataSource dataSource;

    public void reportIssue(IssueDTO issueToReport) throws SQLException{

        String query = "INSERT INTO Issue (title, issue_description, issue_priority, issue_image, issue_type, issue_status, tags, report_time, reporter_id, resolver_id, project_id) VALUES "+
                        "(?, ?, ?, ?, ?::IssueType, ?::IssueStatus, ?, ?, ?, ?, ?);";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, issueToReport.getTitle());
            statement.setString(2, issueToReport.getDescription());
            statement.setInt(3, issueToReport.getPriority());

            if (issueToReport.getImage() != null) {
                statement.setBytes(4, issueToReport.getImage());
            } else {
                statement.setNull(4, Types.BINARY);
            }

            statement.setString(5, issueToReport.getType().toString());
            statement.setString(6, issueToReport.getStatus().toString());
            statement.setString(7, issueToReport.getTags());
            statement.setTimestamp(8, new Timestamp(issueToReport.getReportDate().getTime()));
            statement.setInt(9, issueToReport.getReportingUser().getId());
            statement.setNull(10, Types.INTEGER);
            statement.setInt(11, issueToReport.getRelatedProject().getId());


            statement.executeUpdate();


        }

    }

    public List<IssueDTO> searchIssues(String title, String status, String tags, String type, Integer priority, Integer resolverId, Integer reporterId, Integer projectId) throws SQLException{

        List<IssueDTO> searchResult = null;

        StringBuilder query = new StringBuilder("SELECT issue_id, title FROM issue WHERE ");
        List<Object> searchParam = new ArrayList<>();

        query.append("project_id = ?");
        searchParam.add(projectId);

        if(resolverId != null){
            query.append(" AND resolver_id = ?");
            searchParam.add(resolverId);
        }

        if(reporterId != null){
            query.append(" AND reporter_id = ?");
            searchParam.add(reporterId);
        }

        if (title != null && !title.isEmpty()) {
            query.append(" AND title ILIKE ?");
            searchParam.add("%" + title + "%");
        }
        if (status != null && !status.isEmpty()) {
            query.append(" AND status = ?::IssueStatus");
            searchParam.add(status);
        }
        if (type != null && !type.isEmpty()) {
            query.append(" AND type = ?");
            searchParam.add(type);
        }
        if (priority != null) {
            query.append(" AND priority = ?");
            searchParam.add(priority);
        }
        if (tags != null && !tags.isEmpty()) {
            query.append(" AND tags ILIKE ?");
            searchParam.add("%" + tags + "%");
        }


        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {


            for(int i = 0; i < searchParam.size(); i++){

                statement.setObject( i + 1, searchParam.get(i));

            }

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                IssueDTO issueFounded = new IssueDTO();

                issueFounded.setId(rs.getInt("issue_id"));
                issueFounded.setTitle(rs.getString("title"));


                searchResult.add(issueFounded);


            }

            rs.close();

        }

        return searchResult;

    }


}
