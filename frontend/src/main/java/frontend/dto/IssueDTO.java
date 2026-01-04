package frontend.dto;

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

    public void setTypeWithString(String type) {

        switch (type) {
            case "Bug":
                this.type = IssueTypeDTO.BUG;
                break;
            case "Question":
                this.type = IssueTypeDTO.QUESTION;
                break;
            case "Documentation":
                this.type = IssueTypeDTO.DOCUMENTATION;
                break;
            case "Feature":
                this.type = IssueTypeDTO.FEATURE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
    
}