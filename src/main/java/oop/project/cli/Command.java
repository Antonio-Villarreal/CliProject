package oop.project.cli;

import com.google.common.base.Splitter;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

/**
 * Represents a subcommand with user defined arguments.
 *
 * Example:
 */
public class Command extends Parser {

    /* CONSTRUCTORS */
    /**
     * Constructs a Command with a specified name and identifier.
     * This constructor initializes a command without a description, setting up the fundamental identity
     * parameters but leaving the description as null.
     *
     * @param name The display name of the command, used for identifying the command in a user-friendly manner.
     * @param identifier A unique identifier for the command, used for command parsing and execution handling.
     */
    public Command(String name, String identifier) {
        super(name, identifier);
    }

    /**
     * Constructs a Command with a specified name, identifier, and description.
     * This constructor is used when a command needs a description for help messages or detailed displays,
     * providing users or developers with more context about the command's purpose and usage.
     *
     * @param name The display name of the command.
     * @param identifier A unique identifier for the command.
     * @param description A brief description of what the command does, enhancing clarity in help messages or documentation.
     */
    public Command(String name, String identifier, String description) {
        super(name, identifier, description);
    }

    /* HELP MESSAGE */

    /**
     * Retrieves a message containing information about the command.
     * <p>
     * This method constructs a message containing details on all the arguments of the current Command object
     * such as their name, type, and whether it is required. The message is formatted for display.
     *
     * @return A formatted message containing information about the command, consisting of its arguments' help messages.
     */
    public String getMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append("\t").append(identifier).append("\t");
        for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
            Argument argument = entry.getValue();
            msg.append(argument.getMessage());
        }
        return msg.toString();
    }

    /**
     * Prints all the arguments and commands of this command object.
     *
     * The message is formatted and displays information about the command including its description, usage syntax,
     * arguments, and optional arguments.
     */
    public void printHelpMessage() {
        System.out.println(name);
        System.out.println();
        if(description != null) {
            System.out.println(description);
            System.out.println();
        }
        System.out.println("Usage: " + identifier + " [ARGUMENTS] [COMMAND]");
        System.out.println();
        System.out.println("Arguments:");
        for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
            Argument argument = entry.getValue();
            System.out.println(argument.getMessage());
        }
        System.out.println();
        System.out.println();
        System.out.println("Optional Arguments:");
        System.out.println("\t--help\tMSG: Show the help message.");
    }

    /* PARSING METHODS */

    protected void parseArgs(List<String> tokens) throws Exception {
        if(!(Objects.equals(tokens.getFirst(), identifier))) {
            throw new Exception("Incorrect Identifier in Command");
        }
        tokens.removeFirst();

        if (Objects.equals(tokens.getFirst(), "-h") || Objects.equals(tokens.getFirst(), "--help")) {
            printHelpMessage();
        } else {
            parse(tokens);
        }
    }
}
