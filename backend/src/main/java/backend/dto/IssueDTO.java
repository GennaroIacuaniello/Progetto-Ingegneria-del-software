package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO {

    private Integer id;
    private String title;
    private String description;
    private IssueTypeDTO type;
    private IssueStatusDTO status;
    private String tags;
    private Date reportDate;
    private Date resolutionDate;
    private UserDTO reportingUser;
    private ProjectDTO relatedProject;

    private Integer priority;
    private byte[] image;
    private UserDTO assignedDeveloper;

}