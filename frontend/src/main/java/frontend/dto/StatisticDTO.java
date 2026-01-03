package frontend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StatisticDTO {

    private List<UserDTO> developers = new ArrayList<>();
    private List<IssueDTO> openIssues = new ArrayList<>();
    private List<IssueDTO> closedIssues = new ArrayList<>();
    private List<Integer> numOpenIssues = new ArrayList<>();
    private List<Integer> numClosedIssues = new ArrayList<>();
    private Integer numIssuesNotAssigned = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Duration> averageResolutionDurations = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Duration totalAverageResolutionDuration = Duration.ZERO;

    public StatisticDTO(){
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

    public List<Integer> getNumOpenIssues() {
        return numOpenIssues;
    }

    public void setNumOpenIssues(List<Integer> numOpenIssues) {
        this.numOpenIssues = numOpenIssues;
    }

    public List<Integer> getNumClosedIssues() {
        return numClosedIssues;
    }

    public void setNumClosedIssues(List<Integer> numClosedIssues) {
        this.numClosedIssues = numClosedIssues;
    }

    public List<Duration> getAverageResolutionDurations() {
        return averageResolutionDurations;
    }

    public void setAverageResolutionDurations(List<Duration> averageResolutionDurations) {
        this.averageResolutionDurations = averageResolutionDurations;
    }

    public Integer getNumIssuesNotAssigned() {
        return numIssuesNotAssigned;
    }

    public void setNumIssuesNotAssigned(Integer numIssuesNotAssigned) {
        this.numIssuesNotAssigned = numIssuesNotAssigned;
    }

    public Duration getTotalAverageResolutionDuration() {
        return totalAverageResolutionDuration;
    }

    public void setTotalAverageResolutionDuration(Duration totalAverageResolutionDuration) {
        this.totalAverageResolutionDuration = totalAverageResolutionDuration;
    }

}