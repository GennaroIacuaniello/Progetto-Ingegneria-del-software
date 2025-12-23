package frontend.model;

import java.awt.*;
import java.sql.Date;
import java.util.List;

public class IssueBuilder {

    private String title;
    private String description;
    private IssueType type;
    private IssueStatus status;
    private List<String> tags;
    private Date reportDate;
    private Date resolutionDate;
    private User reportingUser;
    private Project relatedProject;

    private int priority = 2;
    private Image image = null;

    private Developer assignedDeveloper = null;

    public IssueBuilder(String title, String description, IssueType type, IssueStatus status,
                 List<String> tags, Date reportDate, Date resolutionDate, User reportingUser, Project relatedProject) {

        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
        this.tags = tags;
        this.reportDate = reportDate;
        this.resolutionDate = resolutionDate;
        this.reportingUser = reportingUser;
        this.relatedProject = relatedProject;
    }

    public IssueBuilder withPriority (int priority){

        this.priority = priority;

        return this;
    }

    public IssueBuilder withImage (Image image){

        this.image = image;

        return this;
    }

    public IssueBuilder withAssignedDeveloper (Developer developer){

        this.assignedDeveloper = developer;

        return this;
    }

    public Issue build(){

        return new Issue(title, description, type, status, tags,
                         reportDate, resolutionDate, reportingUser, relatedProject,
                         priority, image, assignedDeveloper);

    }

}
