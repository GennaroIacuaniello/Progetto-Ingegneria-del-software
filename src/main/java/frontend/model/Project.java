package frontend.model;

import frontend.exception.InvalidDeveloper;
import frontend.exception.InvalidTeam;

import java.util.List;

public class Project {

    private String id;
    private String name;
    private List<Issue> issues;
    private List<Team> teams;
    private List<Developer> developers;

    public Project(String name, List<Team> teams, List<Developer> developers) throws InvalidTeam, InvalidDeveloper {

        if( teams == null || teams.isEmpty() )
            throw new InvalidTeam("Teams cannot be null or empty");

        if( developers == null || developers.isEmpty() )
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.name = name;
        this.issues = null;
        this.teams = teams;
        this.developers = developers;
    }

    public Project(String name, List<Issue> issues, List<Team> teams, List<Developer> developers) throws InvalidTeam, InvalidDeveloper {

        this(name, teams, developers);

        this.issues = issues;
    }

    public Project(String id, String name) {

        this.id = id;
        this.name = name;
    }

    public Project(String id) {
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

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }
}