package backend.dto;

import backend.exception.InvalidDeveloper;

import java.util.List;

public class TeamDTO {

    private Integer id;
    private String name;
    private ProjectDTO project;
    private List<UserDTO> developers;

    public TeamDTO(){
        //Empty constructor needed for jackson
    }

    public TeamDTO(Integer id, String name, ProjectDTO project, List<UserDTO> developers) throws InvalidDeveloper {

        this.id = id;
        this.name = name;
        this.project = project;
        this.developers = developers;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<UserDTO> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<UserDTO> developers) {
        this.developers = developers;
    }
}