package backend.model;

import backend.exception.InvalidIssueCreation;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
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
    private byte[] image = null;
    private Developer assignedDeveloper = null;


    public IssueBuilder() {
        //Empty constructor needed for jackson
    }

    public IssueBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public IssueBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public IssueBuilder withType(IssueType type) {
        this.type = type;
        return this;
    }

    public IssueBuilder withStatus(IssueStatus status) {
        this.status = status;
        return this;
    }

    public IssueBuilder withTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public IssueBuilder withDates(Date reportDate, Date resolutionDate) {
        this.reportDate = reportDate;
        this.resolutionDate = resolutionDate;
        return this;
    }

    public IssueBuilder withReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
        return this;
    }

    public IssueBuilder withRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
        return this;
    }

    public IssueBuilder withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public IssueBuilder withImage(byte[] image) {
        this.image = image;
        return this;
    }

    public IssueBuilder withAssignedDeveloper(Developer assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
        return this;
    }


    public Issue build() {

        if (title == null || description == null || type == null ||
                status == null || tags == null || reportDate == null ||
                reportingUser == null || relatedProject == null) {

            throw new InvalidIssueCreation("Issue creation failed: some mandatory fields are missing.");
        }

        return new Issue(title, description, type, status, tags,
                reportDate, resolutionDate, reportingUser, relatedProject,
                priority, image, assignedDeveloper);
    }
}