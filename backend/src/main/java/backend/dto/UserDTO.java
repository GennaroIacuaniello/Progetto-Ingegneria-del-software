package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;
    private String email;
    private String password;

    private Integer role; //0 guest, 1 developer, 2 admin

    private List<IssueDTO> reportedIssues = new ArrayList<>();

    private List<IssueDTO> assignedIssues = new ArrayList<>();      //only developer or admin
    private List<ProjectDTO> projects = new ArrayList<>();
    private List<TeamDTO> teams = new ArrayList<>();

}