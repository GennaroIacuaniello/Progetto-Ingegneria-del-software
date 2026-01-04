package backend.model;

import backend.exception.InvalidDeveloper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Team {

    private String name;
    private Project project;
    private List<Developer> developers;

    @SuppressWarnings("unused")
    public Team(String name, Project project, List<Developer> developers) throws InvalidDeveloper {

        if (developers == null || developers.isEmpty())
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.name = name;
        this.project = project;

        this.developers = new ArrayList<>();

        this.developers.addAll(developers);

    }

}