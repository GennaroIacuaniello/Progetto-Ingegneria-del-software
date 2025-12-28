package backend.database.dao;

import backend.dto.IssueDTO;

import java.sql.SQLException;

public interface IssueDAO {

    void reportIssue(IssueDTO issueToReport) throws SQLException;

}
