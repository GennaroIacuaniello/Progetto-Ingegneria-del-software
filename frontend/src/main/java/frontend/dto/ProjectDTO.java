package frontend.dto;

import java.util.List;

public class ProjectDTO {

    private Integer id;
    private String name;
    private List<IssueDTO> issues;
    private List<TeamDTO> teams;
    private List<UserDTO> developers;

    public ProjectDTO(){
        //Empty constructor needed for jackson
    }

    public ProjectDTO(Integer id, String name, List<TeamDTO> teams, List<UserDTO> developers) {

        this.id = id;
        this.name = name;
        this.issues = null;
        this.teams = teams;
        this.developers = developers;

    }

    public ProjectDTO(Integer id, String name, List<IssueDTO> issues, List<TeamDTO> teams, List<UserDTO> developers) {

        this(id, name, teams, developers);

        this.issues = issues;
    }

    public ProjectDTO(Integer id, String name) {

        this.id = id;
        this.name = name;
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

    public List<IssueDTO> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueDTO> issues) {
        this.issues = issues;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public List<UserDTO> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<UserDTO> developers) {
        this.developers = developers;
    }
}