package hu.ponte.homework.pontevotehomework.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiError {

    private final String message;

    private final HttpStatus status;

    private final ZonedDateTime timestamp;

    public ApiError(String message, String status, String timestamp) {
        this.message = message;
        this.status = HttpStatus.valueOf(status);
        this.timestamp = ZonedDateTime.parse(timestamp);
    }

    public String getMessage() {
        return message;
    }



}
