package fpt.capstone.payroll;


class ValidationExceptionResponse {
    private final String name;
    private final String message;

    public ValidationExceptionResponse(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}

public class ValidationException extends RuntimeException {
    private final String name;
    private final String validateMessage;

    public ValidationException(String name, String validateMessage) {
        super("Validate error for " + name + " because of " + validateMessage);
        this.name = name;
        this.validateMessage = validateMessage;
    }


    public String getName() {
        return name;
    }

    public String getValidateMessage() {
        return validateMessage;
    }

    ValidationExceptionResponse getResponse() {
        return new ValidationExceptionResponse(name, validateMessage);
    }
}
