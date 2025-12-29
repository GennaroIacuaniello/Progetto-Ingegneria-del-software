package backend.database.dao;

import backend.dto.IssueDTO;
import backend.dto.IssueStatusDTO;
import backend.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface IssueDAO {

    void reportIssue(IssueDTO issueToReport) throws SQLException;

    List<IssueDTO> searchIssues(String title, String status, String tags, String type, Integer priority, Integer resolver_id, Integer reporterId, Integer project_id) throws SQLException;

    IssueDTO getIssueById(Integer issueId) throws SQLException;

    boolean updateStatus(Integer id, IssueStatusDTO newStatus) throws SQLException;

    UserDTO assignIssueToDeveloperByEmail(Integer id, String resolverEmail) throws SQLException;

}
