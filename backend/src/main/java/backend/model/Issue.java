package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    private String title;
    private String description;
    private IssueType type;
    private IssueStatus status;
    private List<String> tags;
    private Date reportDate;
    private Date resolutionDate;
    private User reportingUser;
    private Project relatedProject;

    private int priority;
    private byte[] image;
    private Developer assignedDeveloper;

}