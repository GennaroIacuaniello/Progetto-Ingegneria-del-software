package backend.model;

import java.util.ArrayList;
import java.util.List;

public class Developer extends User {

    private List<Issue> assignedIssues;
    private List<Project> projects;
    private List<Team> teams;

    public Developer(String email, String hashedPassword, List<Issue> reportedIssues,
                     List<Issue> assignedIssues, List<Project> projects, List<Team> teams) {

        super(email, hashedPassword, reportedIssues);

        this.assignedIssues = new ArrayList<>();

        if(assignedIssues != null)
            this.assignedIssues.addAll(assignedIssues);

        this.projects = new ArrayList<>();

        if(projects != null)
            this.projects.addAll(projects);

        this.teams = new ArrayList<>();

        if(teams != null)
            this.teams.addAll(teams);

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

    public void addAssignedIssue (Issue assignedIssue){
        this.assignedIssues.add(assignedIssue);
    }

    public void addProject (Project project){
        this.projects.add(project);
    }

    public void addTeam (Team team){
        this.teams.add(team);
    }


}