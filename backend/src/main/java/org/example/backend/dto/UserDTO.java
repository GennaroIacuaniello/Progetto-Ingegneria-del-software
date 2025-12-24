package backend.dto;

import java.util.List;

public class UserDTO {

    private int id;
    private String email;
    private final String hashedPassword;
    private List<IssueDTO> reportedIssues;

    public UserDTO(int id, String email, String hashedPassword, List<IssueDTO> reportedIssues) {

        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.reportedIssues = reportedIssues;

    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<IssueDTO> getReportedIssues() {
        return reportedIssues;
    }

    public void setReportedIssues (List<IssueDTO> reportedIssues) {
        this.reportedIssues = reportedIssues;
    }

}