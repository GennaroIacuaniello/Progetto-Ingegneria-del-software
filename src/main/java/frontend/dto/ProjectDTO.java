package frontend.dto;

import frontend.exception.InvalidDeveloper;
import frontend.exception.InvalidTeam;

import java.util.List;

/*TODO

    Package per eccezioni personalizzate

 */

public class ProjectDTO {

    private String id;
    private String name;
    private List<IssueDTO> issues;
    private List<TeamDTO> teams;
    private List<DeveloperDTO> developers;

    public ProjectDTO(String name, List<TeamDTO> teams, List<DeveloperDTO> developers) throws InvalidTeam, InvalidDeveloper {

        if( teams == null || teams.isEmpty() )
            throw new InvalidTeam("Teams cannot be null or empty");

        if( developers == null || developers.isEmpty() )
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.name = name;
        this.issues = null;
        this.teams = teams;
        this.developers = developers;
    }

    public ProjectDTO(String name, List<IssueDTO> issues, List<TeamDTO> teams, List<DeveloperDTO> developers) throws InvalidTeam, InvalidDeveloper {

        this(name, teams, developers);

        this.issues = issues;
    }

    public ProjectDTO(String id, String name) {

        this.id = id;
        this.name = name;
    }

    public ProjectDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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