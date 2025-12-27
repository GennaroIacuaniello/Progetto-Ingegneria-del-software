package backend.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private final String password;
    private ArrayList<Issue> reportedIssues;

    public User(String email, String password, List<Issue> reportedIssues) {

        this.email = email;
        this.password = password;

        this.reportedIssues = new ArrayList<>();

        if(reportedIssues != null)
            this.reportedIssues.addAll(reportedIssues);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public List<Issue> getReportedIssues() {
        return reportedIssues;
    }

    public void setReportedIssues (List<Issue> reportedIssues) {
        this.reportedIssues = (ArrayList<Issue>) reportedIssues;
    }

    public void addReportedIssue (Issue reportedIssue){
        this.reportedIssues.add(reportedIssue);
    }

}