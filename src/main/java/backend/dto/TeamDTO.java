package backend.dto;

import backend.exception.InvalidDeveloper;

import java.util.List;

public class TeamDTO {

    private int id;
    private String name;
    private ProjectDTO project;
    private List<DeveloperDTO> developers;

    public TeamDTO(int id, String name, ProjectDTO project, List<DeveloperDTO> developers) throws InvalidDeveloper {

        if (developers == null || developers.isEmpty())
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.id = id;
        this.name = name;
        this.project = project;
        this.developers = developers;

    }

    public int getId() {
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

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public List<DeveloperDTO> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<DeveloperDTO> developers) {
        this.developers = developers;
    }
}