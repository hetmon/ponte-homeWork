package hu.ponte.homework.pontevotehomework.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiError {

    private final String message;

    private final String status;

    private final String timeStamp;



    public ApiError(String message, HttpStatus status, ZonedDateTime timeStamp) {
        this.message = message;
        this.status = status.toString();
        this.timeStamp = timeStamp.toString();
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
