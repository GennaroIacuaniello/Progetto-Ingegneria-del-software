package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String email;
    private String password;
    private ArrayList<Issue> reportedIssues;

    public User(String email, String password, List<Issue> reportedIssues) {

        this.email = email;
        this.password = password;

        this.reportedIssues = new ArrayList<>();

        if(reportedIssues != null)
            this.reportedIssues.addAll(reportedIssues);

    }

}