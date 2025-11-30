package model;

import java.util.List;

public class Admin extends Developer{

    public Admin (String email, String hashedPassword, List<Issue> reportedIssues,
                  List<Issue> assignedIssue, List<Project> projects, List<Team> teams) {

        super(email, hashedPassword, reportedIssues, assignedIssue, projects, teams);
    }

    public void assignIssue() {}

    public void viewIssueDashBoard() {}

    public void viewTeamReport() {}

    public void createDeveloper() {}
}