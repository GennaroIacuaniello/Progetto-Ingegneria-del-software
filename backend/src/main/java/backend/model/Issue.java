package backend.model;

import java.awt.*;
import java.util.List;
import java.util.Date;

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

    Issue(String title, String description, IssueType type, IssueStatus status,
          List<String> tags, Date reportDate, Date resolutionDate, User reportingUser, Project relatedProject,
          int priority, byte[] image, Developer assignedDeveloper) {

        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
        this.tags = tags;
        this.reportDate = reportDate;
        this.resolutionDate = resolutionDate;
        this.reportingUser = reportingUser;
        this.relatedProject = relatedProject;

        this.priority = priority;
        this.image = image;
        this.assignedDeveloper = assignedDeveloper;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public User getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
    }

    public Developer getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public void setAssignedDeveloper(Developer assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
    }

    public Project getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
    }
}