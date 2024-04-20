package oop.project.cli;

/**
 * This class handles custom exceptions
 */
public class CLIException extends Exception {
    public CLIException(String message) {
        super(message);
    }
}

/* PARSE EXCEPTIONS */

/**
 * Constructs an {@code ParseException} with the user specified error message.
 */
class ParseException extends CLIException {
    public ParseException(String message) {
        super("Parsing Exception: " + message);
    }
}

/* VALIDATION EXCEPTIONS */
/**
 * Constructs an {@code ValidationException} with the user specified error message.
 */
class ValidationException extends CLIException {
    public ValidationException(String message) {
        super("Validation Exception: " + message);
    }
}
/**
 * Constructs an {@code ValidationFunctionException} that tells the user when their argument
 * fails validation.
 */
class ValidationFunctionException extends ValidationException {
    public ValidationFunctionException(String argument) {
        super("'" + argument + "' argument does not meet the limits of the provided validation function.");
    }
}
/**
 * Constructs an {@code ValidationCustomConversionException} that tells the user when their
 * argument fails to convert to the custom type.
 */
class ValidationCustomConversionException extends ValidationException {
    public ValidationCustomConversionException(String argument, String type) {
        super("'" + argument + "''s custom class (" + type + ") failed to convert.");
    }
}

/**
 * Constructs an {@code ValidationDefaultConversionException} with the conversion of the user's custom class.
 * helps in diagnosing the type compatibility or format issues.
 * Exception thrown when the default conversion method (usually a constructor or valueOf method)
 *  fails to convert a provided input string to the required type.
 */
class ValidationDefaultConversionException extends ValidationException {
    public ValidationDefaultConversionException(String argument, String type) {
        super("'" + argument + "''s custom class (" + type + ") failed to convert.");
    }
}

