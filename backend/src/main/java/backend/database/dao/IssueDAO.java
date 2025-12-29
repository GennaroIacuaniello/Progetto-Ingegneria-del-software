package backend.database.dao;

import backend.dto.IssueDTO;

import java.sql.SQLException;
import java.util.List;

public interface IssueDAO {

    void reportIssue(IssueDTO issueToReport) throws SQLException;

    List<IssueDTO> searchIssues(String title, String status, String tags, String type, Integer priority, Integer resolver_id, Integer reporterId, Integer project_id) throws SQLException;

}
