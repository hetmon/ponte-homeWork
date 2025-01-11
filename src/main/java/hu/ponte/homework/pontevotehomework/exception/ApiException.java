package hu.ponte.homework.pontevotehomework.exception;

import org.springframework.http.HttpStatus;

public class ApiException {

    private final String message;

    private final HttpStatus status;

    private final String timestamp;

    public ApiException(String message, String status, String timestamp) {
        this.message = message;
        this.status = HttpStatus.valueOf(status);
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
