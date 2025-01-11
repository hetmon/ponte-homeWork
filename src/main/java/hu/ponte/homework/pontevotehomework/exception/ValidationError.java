package hu.progmasters.webshop.exception;


import java.util.ArrayList;
import java.util.List;

public class ValidationError {


    private final List<CustomFieldError> errors = new ArrayList<>();

    public void addingCustomFieldError(String field, String message) {
        errors.add(new CustomFieldError(field,message));
    }

    public List<CustomFieldError> getErrors() {
        return errors;
    }

}
