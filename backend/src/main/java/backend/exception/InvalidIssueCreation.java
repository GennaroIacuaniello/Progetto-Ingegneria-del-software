package backend.exception;

public class InvalidIssueCreation extends RuntimeException {
    public InvalidIssueCreation(String message) {
        super(message);
    }
}
