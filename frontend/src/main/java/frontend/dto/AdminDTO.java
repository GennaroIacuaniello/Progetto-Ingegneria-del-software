package frontend.dto;

import java.util.List;

public class AdminDTO extends DeveloperDTO{

    public AdminDTO() {
        //Empty constructor needed for jackson
    }

    public AdminDTO (String email, String hashedPassword, List<IssueDTO> reportedIssues,
                  List<IssueDTO> assignedIssue, List<ProjectDTO> projects, List<TeamDTO> teams) {

        super(email, hashedPassword, reportedIssues, assignedIssue, projects, teams);
    }

}