package backend.model;

import backend.exception.InvalidDeveloper;
import backend.exception.InvalidTeam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Project {

    private int id;
    private String name;
    private List<Issue> issues;
    private List<Team> teams;
    private List<Developer> developers;

    @SuppressWarnings("unused")
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

}