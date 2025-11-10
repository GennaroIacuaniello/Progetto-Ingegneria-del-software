package model;

import java.util.List;

public class Developer extends User {

    private List<Issue> assignedIssues;
    private List<Project> projects;
    private List<Team> teams;

    public Developer(String email, String hashedPassword, List<Issue> reportedIssues,
                     List<Issue> assignedIssue, List<Project> projects, List<Team> teams) {

        super(email, hashedPassword, reportedIssues);

        this.assignedIssues = assignedIssue;
        this.projects = projects;
        this.teams = teams;
    }

    public List<Issue> getAssignedIssues() {
        return assignedIssues;
    }

    public void setAssignedIssues(List<Issue> assignedIssues) {
        this.assignedIssues = assignedIssues;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void viewAssignedIssue() {}
}