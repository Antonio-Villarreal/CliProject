package oop.project.cli;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;


/**
 * ArgumentParser is a utility class designed to parse and handle command-line arguments for applications.
 * It supports both positional arguments and named flags, as well as sub-commands with their own sets
 * of arguments. It also provides functionality to print help messages based on the defined arguments
 * and commands, and can validate argument values using custom validation functions.
 */
public class ArgumentParser extends Parser {
    //Storage
    /**
     * Stores the mapping of command names to Command objects that the parser can recognize and handle.
     */
    protected Map<String, Command> commands = new LinkedHashMap<>();

    /* CONSTRUCTORS */
    /**
     * Constructs a basic ArgumentParser with the specified name and identifier. This constructor
     * delegates to the superclass Parser for basic initialization.
     *
     * @param name       The name of the program or utility that this ArgumentParser instance is for.
     * @param identifier A unique identifier used in the command-line invocation of this program or utility.
     */
    public ArgumentParser(String name, String identifier) {
        super(name, identifier);
    }

    /**
     * Constructs an ArgumentParser with a name, identifier, and description. This constructor
     * delegates to the superclass Parser to include a description in the initialization process.
     *
     * @param name        The name of the program.
     * @param identifier  The command-line identifier for the program.
     * @param description A description of what the program or utility does.
     */
    public ArgumentParser(String name, String identifier, String description) {
        super(name, identifier, description);
    }

    /* COMMAND METHOD */
    /**
     * Adds a Command object to the parser. If a command with the same name already exists,
     * an IllegalArgumentException is thrown to avoid command name conflicts.
     *
     * @param command The Command object to add to the parser.
     */
    public void addCommand(Command command) {
        storeCommandInMap(command.name(), command);
    }

    /* MAP STORAGE METHODS */
    /**
     * Retrieves a Command object from the map of commands by its identifier. If the command is not found,
     * an IllegalArgumentException is thrown.
     *
     * @param identifier The unique identifier of the command to retrieve.
     * @return The Command object associated with the given identifier.
     */
    private Command getCommandFromMap(String identifier) {
        if (!commands.containsKey(identifier)) {
            throw new IllegalArgumentException("Command with name '" + identifier + "' not found.");
        }
        return commands.get(identifier);
    }

    /**
     * Stores a Command object to the map of commands based on its identifier. If the command already exists,
     * an IllegalArgumentException is thrown.
     *
     * @param identifier The unique identifier that will be used to retrieve the command.
     * @param command The Command object that will be stored.
     */
    private void storeCommandInMap(String identifier, Command command) {
        if(commands.containsKey(identifier)) {
            throw new IllegalArgumentException("Value with name '" + identifier + "' already exists.");
        }
        commands.put(identifier, command);
    }

    /* HELP MESSAGE */
    /**
     * Prints all the arguments and commands attached to this Command object.
     * </p>
     * The message is formatted and displays information about the Command including its description, usage syntax,
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
        if (!arguments.isEmpty()) {
            System.out.println();
            System.out.println("Arguments:");
            for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
                Argument argument = entry.getValue();
                System.out.println(argument.getMessage());
            }
        }
        if (!commands.isEmpty()) {
            System.out.println();
            System.out.println("Commands:");
            for (Map.Entry<String, Command> entry : commands.entrySet()) {
                Command command = entry.getValue();
                System.out.println(command.getMessage());
            }
        }
        System.out.println();
        System.out.println("Optional Arguments:");
        System.out.println("\t--help\tMSG: Show the help message.");
    }
}

