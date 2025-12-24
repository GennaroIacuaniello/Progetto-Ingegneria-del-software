package backend.exception;

public class InvalidDeveloper extends RuntimeException {

    public InvalidDeveloper(){}

    public InvalidDeveloper(String message) {
        super(message);
    }
}
