package frontend.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private int id;
    private String email;
    private String password; // Solo per invio login, sarà null in ricezione
    private int role; // 0=User, 1=Dev, 2=Admin

    private List<IssueDTO> reportedIssues;
    private List<IssueDTO> assignedIssues;


    private List<ProjectDTO> projects;
    private List<TeamDTO> teams;

    public UserDTO() {
        this.projects = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.reportedIssues = new ArrayList<>();
        this.assignedIssues = new ArrayList<>();
    }

    // Costruttore per la login request
    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public List<IssueDTO> getReportedIssues() { return reportedIssues; }
    public void setReportedIssues(List<IssueDTO> reportedIssues) { this.reportedIssues = reportedIssues; }

    public List<IssueDTO> getAssignedIssues() { return assignedIssues; }
    public void setAssignedIssues(List<IssueDTO> assignedIssues) { this.assignedIssues = assignedIssues; }

    public List<ProjectDTO> getProjects() { return projects; }
    public void setProjects(List<ProjectDTO> projects) { this.projects = projects; }

    public List<TeamDTO> getTeams() { return teams; }
    public void setTeams(List<TeamDTO> teams) { this.teams = teams; }

    // Helper utili per l'UI
    public boolean isAdmin() { return role == 2; }
    public boolean isDeveloper() { return role == 1 || role == 2; }
}