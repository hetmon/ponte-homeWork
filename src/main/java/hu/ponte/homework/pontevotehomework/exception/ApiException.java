package hu.progmasters.webshop.exception;

import org.springframework.http.HttpStatus;

public class ApiException {

    private final String message;

    private final HttpStatus status;

    private final String timestamp;

    public ApiException(String message, HttpStatus status, String timestamp) {
        this.message = message;
        this.status = status;
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
