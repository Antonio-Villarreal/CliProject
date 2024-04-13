package oop.project.cli;

public class CLIException extends Exception {
    public CLIException(String message) {
        super(message);
    }
}

class UnknownCommandException extends CLIException {
    public UnknownCommandException(String commandName) {
        super("Unknown command: " + commandName);
    }
}

class InvalidArgumentException extends CLIException {
    public InvalidArgumentException(String message) {
        super("Invalid argument: " + message);
    }
}

