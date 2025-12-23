package backend.model;

import backend.exception.InvalidDeveloper;
import backend.exception.InvalidTeam;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private int id;
    private String name;
    private List<Issue> issues;
    private List<Team> teams;
    private List<Developer> developers;

    public Project(int id, String name, List<Team> teams, List<Developer> developers) throws InvalidTeam, InvalidDeveloper {

        if( teams == null || teams.isEmpty() )
            throw new InvalidTeam("Teams cannot be null or empty");

        if( developers == null || developers.isEmpty() )
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.id = id;

        this.name = name;

        this.issues = null;

        this.teams = new ArrayList<>();

        this.teams.addAll(teams);

        this.developers = developers;
    }

    public Project(int id, String name, List<Issue> issues, List<Team> teams, List<Developer> developers) throws InvalidTeam, InvalidDeveloper {

        this(id, name, teams, developers);

        this.issues = new ArrayList<>();

        if(issues != null)
            this.issues.addAll(issues);
    }

    public Project(int id, String name) {

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

    public void addIssue (Issue issue){
        this.issues.add(issue);
    }

}