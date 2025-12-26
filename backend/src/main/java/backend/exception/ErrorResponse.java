package backend.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String messaggio;
    private LocalDateTime data;
    private int codiceStatus;

    public ErrorResponse(String messaggio, int codiceStatus) {
        this.messaggio = messaggio;
        this.codiceStatus = codiceStatus;
        this.data = LocalDateTime.now();
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public int getCodiceStatus() {
        return codiceStatus;
    }

    public void setCodiceStatus(int codiceStatus) {
        this.codiceStatus = codiceStatus;
    }

}