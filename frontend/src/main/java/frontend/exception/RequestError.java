package frontend.exception;

public class RequestError extends RuntimeException {

    public RequestError(){}

    public RequestError(String message) {
        super(message);
    }

}
