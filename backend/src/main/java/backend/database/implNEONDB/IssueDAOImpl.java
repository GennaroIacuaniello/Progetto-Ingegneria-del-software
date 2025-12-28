package backend.database.implNEONDB;

import backend.database.DatabaseConnection;
import backend.database.dao.IssueDAO;
import backend.dto.IssueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;

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

}
