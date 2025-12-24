package backend.dto;

import java.util.List;

public class AdminDTO extends DeveloperDTO {

    public AdminDTO (int id, String email, String hashedPassword, List<IssueDTO> reportedIssues,
                     List<IssueDTO> assignedIssue, List<ProjectDTO> projects, List<TeamDTO> teams) {

        super(id, email, hashedPassword, reportedIssues, assignedIssue, projects, teams);
    }

}