package backend.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

}