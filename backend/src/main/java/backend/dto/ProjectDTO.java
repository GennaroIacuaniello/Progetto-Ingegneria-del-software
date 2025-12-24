package backend.dto;

import backend.exception.InvalidDeveloper;
import backend.exception.InvalidTeam;

import java.util.List;

public class ProjectDTO {

    private int id;
    private String name;
    private List<IssueDTO> issues;
    private List<TeamDTO> teams;
    private List<DeveloperDTO> developers;

    public ProjectDTO(int id, String name, List<TeamDTO> teams, List<DeveloperDTO> developers) throws InvalidTeam, InvalidDeveloper {

        if( teams == null || teams.isEmpty() )
            throw new InvalidTeam("Teams cannot be null or empty");

        if( developers == null || developers.isEmpty() )
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.id = id;
        this.name = name;
        this.issues = null;
        this.teams = teams;
        this.developers = developers;
    }

    public ProjectDTO(int id, String name, List<IssueDTO> issues, List<TeamDTO> teams, List<DeveloperDTO> developers) throws InvalidTeam, InvalidDeveloper {

        this(id, name, teams, developers);

        this.issues = issues;
    }

    public ProjectDTO(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IssueDTO> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueDTO> issues) {
        this.issues = issues;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public List<DeveloperDTO> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<DeveloperDTO> developers) {
        this.developers = developers;
    }
}