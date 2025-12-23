package backend.exception;

public class InvalidTeam extends RuntimeException {

    public InvalidTeam(){}

    public InvalidTeam(String message) {
        super(message);
    }

}
