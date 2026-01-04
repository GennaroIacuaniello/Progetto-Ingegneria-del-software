package backend.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Admin extends Developer{

    @SuppressWarnings("unused")
    public Admin (String email, String hashedPassword, List<Issue> reportedIssues,
                  List<Issue> assignedIssue, List<Project> projects, List<Team> teams) {

        super(email, hashedPassword, reportedIssues, assignedIssue, projects, teams);
    }

}