package hu.progmasters.webshop.exception;

public class CustomFieldError {

    private final String field;

    private final String message;

    public CustomFieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

}
