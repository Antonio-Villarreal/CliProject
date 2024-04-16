package oop.project.cli;

public class CLIException extends Exception {
    public CLIException(String message) {
        super(message);
    }
}

/* PARSE EXCEPTIONS */

class ParseException extends CLIException {
    public ParseException(String message) {
        super("Parsing Exception: " + message);
    }
}

/* VALIDATION EXCEPTIONS */

class ValidationException extends CLIException {
    public ValidationException(String message) {
        super("Validation Exception: " + message);
    }
}

class ValidationFunctionException extends ValidationException {
    public ValidationFunctionException(String argument) {
        super("'" + argument + "' argument does not meet the limits of the provided validation function.");
    }
}

class ValidationCustomConversionException extends ValidationException {
    public ValidationCustomConversionException(String argument, String type) {
        super("'" + argument + "''s custom class (" + type + ") failed to convert.");
    }
}

class ValidationDefaultConversionException extends ValidationException {
    public ValidationDefaultConversionException(String argument, String type) {
        super("'" + argument + "''s custom class (" + type + ") failed to convert.");
    }
}

