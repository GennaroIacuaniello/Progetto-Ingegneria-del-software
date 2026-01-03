package backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TeamReportDTO {

    private List<UserDTO> developers = new ArrayList<>();
    private List<IssueDTO> openIssues = new ArrayList<>();
    private List<IssueDTO> closedIssues = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Duration> averageResolutionDurations = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Duration totalAverageResolutionDuration = Duration.ZERO;

    public TeamReportDTO(){
        //Empty constructor needed for Jackson
    }

    public List<UserDTO> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<UserDTO> developers) {
        this.developers = developers;
    }

    public List<IssueDTO> getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(List<IssueDTO> openIssues) {
        this.openIssues = openIssues;
    }

    public List<IssueDTO> getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(List<IssueDTO> closedIssues) {
        this.closedIssues = closedIssues;
    }

    public List<Duration> getAverageResolutionDurations() {
        return averageResolutionDurations;
    }

    public void setAverageResolutionDurations(List<Duration> averageResolutionDurations) {
        this.averageResolutionDurations = averageResolutionDurations;
    }

    public Duration getTotalAverageResolutionDuration() {
        return totalAverageResolutionDuration;
    }

    public void setTotalAverageResolutionDuration(Duration totalAverageResolutionDuration) {
        this.totalAverageResolutionDuration = totalAverageResolutionDuration;
    }

}