package backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponse {
    private String message;
    private LocalDateTime data;
    private int codiceStatus;

    public ErrorResponse(String message, int codiceStatus) {
        this.message = message;
        this.codiceStatus = codiceStatus;
        this.data = LocalDateTime.now();
    }

}