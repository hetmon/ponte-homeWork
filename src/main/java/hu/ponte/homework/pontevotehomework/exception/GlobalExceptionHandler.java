package hu.ponte.homework.pontevotehomework.exception;

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

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
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
    public ResponseEntity<ApiException> handleJsonParseException(JsonParseException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException body = new ApiException("JSON_PARSE_ERROR", "Invalid JSON request.", ex.getLocalizedMessage());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiException> defaultErrorHandler(Throwable t) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException body = new ApiException("UNCLASSIFIED_ERROR", "An unexpected error occurred.", t.getLocalizedMessage());

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IdeaNotExistsException.class)
    public ResponseEntity<ApiException> handleIdeaNotExistsException(IdeaNotExistsException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(apiExceptionMake(ex, status), status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiException> handleUserNotFoundException(UserNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(apiExceptionMake(ex, status), status);
    }


    @ExceptionHandler(InvalidVoteException.class)
    public ResponseEntity<ApiException> handleInvalidVoteException(InvalidVoteException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(apiExceptionMake(ex, status), status);
    }

    private <T extends RuntimeException> ApiException apiExceptionMake(T exception, HttpStatus status) {
        return new ApiException(exception.getMessage(), status.toString(), ZonedDateTime.now().toString());
    }
}
