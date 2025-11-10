package model;

import java.util.List;

public class User {

    private String email;
    private final String hashedPassword;
    private List<Issue> reportedIssues;

    public User(String email, String hashedPassword, List<Issue> reportedIssues) {

        this.email = email;
        this.hashedPassword = hashedPassword;
        this.reportedIssues = reportedIssues;
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

    public List<Issue> getReportedIssues() {
        return reportedIssues;
    }

    public void setReportedIssues (List<Issue> reportedIssues) {
        this.reportedIssues = reportedIssues;
    }

    public void viewReportedIssues() {}

    public void reportIssue() {}
}