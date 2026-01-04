package frontend.dto;

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
    private Integer role; // 0=User, 1=Dev, 2=Admin

    private List<IssueDTO> reportedIssues = new ArrayList<>();
    private List<IssueDTO> assignedIssues = new ArrayList<>();

    private List<ProjectDTO> projects = new ArrayList<>();
    private List<TeamDTO> teams = new ArrayList<>();


    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}