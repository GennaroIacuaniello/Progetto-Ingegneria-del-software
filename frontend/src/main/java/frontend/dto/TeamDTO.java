package frontend.dto;

import java.util.List;

public class TeamDTO {

    private Integer id;
    private String name;
    private ProjectDTO project;
    private List<UserDTO> developers;

    public TeamDTO(){
        //Empty constructor needed for jackson
    }

    public TeamDTO(String name, ProjectDTO project, List<UserDTO> developers) {

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