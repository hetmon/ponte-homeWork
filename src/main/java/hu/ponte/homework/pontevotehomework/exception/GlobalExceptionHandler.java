package hu.ponte.homework.pontevotehomework.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Field error:{}", ex.getBindingResult());
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return new ResponseEntity<>(makeValidationError(fieldErrors), HttpStatus.BAD_REQUEST);
    }

    private ValidationError makeValidationError(List<FieldError> fieldErrors) {
        ValidationError validationError = new ValidationError();
        for (FieldError fieldError : fieldErrors) {
            validationError.addingCustomFieldError(
                    fieldError.getField(),
                    messageSource.getMessage(fieldError, Locale.forLanguageTag(messageSource.getMessage(fieldError, Locale.getDefault())))
            );
        }
        return validationError;
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ApiError> handleJsonParseException(JsonParseException ex) {
        log.error("Json parse error:{}", ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError body = new ApiError(ex.getMessage(), status
                ,ZonedDateTime.now());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> defaultErrorHandler(Throwable t) {
        log.error("Internal error:{}", t.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError body = new ApiError(t.getMessage(), status
                ,ZonedDateTime.now());

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IdeaNotExistsException.class)
    public ResponseEntity<ApiError> handleIdeaNotExistsException(IdeaNotExistsException ex) {
        log.error("Idea not exists error:{}", ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(apiExceptionMake(ex, status), status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found error:{}", ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(apiExceptionMake(ex, status), status);
    }


    @ExceptionHandler(InvalidVoteException.class)
    public ResponseEntity<ApiError> handleInvalidVoteException(InvalidVoteException ex) {
        log.error("Invalid vote error:{}", ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(apiExceptionMake(ex, status), status);
    }

    private <T extends RuntimeException> ApiError apiExceptionMake(T exception, HttpStatus status) {
        return new ApiError(exception.getMessage(), status, ZonedDateTime.now());
    }
}
