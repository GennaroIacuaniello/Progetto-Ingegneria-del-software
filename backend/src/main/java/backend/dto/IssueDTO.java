package backend.dto;

import backend.caso.DeveloperDTO;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class IssueDTO {

    private int id;
    private String title;
    private String description;
    private IssueTypeDTO type;
    private IssueStatusDTO status;
    private String tags;
    private Date reportDate;
    private Date resolutionDate;
    private UserDTO reportingUser;
    private ProjectDTO relatedProject;

    private int priority;
    private byte[] image;
    private UserDTO assignedDeveloper;

    public IssueDTO(){
        //Empty constructor needed for jackson
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public IssueTypeDTO getType() {
        return type;
    }

    public void setType(IssueTypeDTO type) {
        this.type = type;
    }

    public IssueStatusDTO getStatus() {
        return status;
    }

    public void setStatus(IssueStatusDTO status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
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

    public UserDTO getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(UserDTO reportingUser) {
        this.reportingUser = reportingUser;
    }

    public UserDTO getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public void setAssignedDeveloper(UserDTO assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
    }

    public ProjectDTO getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(ProjectDTO relatedProject) {
        this.relatedProject = relatedProject;
    }
}