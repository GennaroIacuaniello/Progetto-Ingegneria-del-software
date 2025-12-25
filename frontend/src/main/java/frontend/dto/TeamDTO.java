package frontend.dto;

import java.util.List;

public class TeamDTO {

    private String name;
    private ProjectDTO project;
    private List<DeveloperDTO> developers;

    public TeamDTO(){
        //Empty constructor needed for jackson
    }

    public TeamDTO(String name, ProjectDTO project, List<DeveloperDTO> developers) {

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