package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Integer id;
    private String name;
    private List<IssueDTO> issues;
    private List<TeamDTO> teams;
    private List<UserDTO> developers;


    public ProjectDTO(Integer id, String name) {

        this.id = id;
        this.name = name;
    }

}