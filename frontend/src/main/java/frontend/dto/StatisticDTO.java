package frontend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}