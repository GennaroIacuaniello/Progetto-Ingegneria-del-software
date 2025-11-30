package model;

import java.util.List;

public class Team {

    private String name;
    private Project project;
    private List<Developer> developers;

    public Team(String name, Project project, List<Developer> developers) throws IllegalArgumentException {

        if (developers == null || developers.isEmpty())
            throw new IllegalArgumentException("developers cannot be null or empty");

        this.name = name;
        this.project = project;
        this.developers = developers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }
}