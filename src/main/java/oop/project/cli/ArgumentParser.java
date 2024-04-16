package oop.project.cli;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import com.google.common.base.Splitter;

import java.util.*;


/**
 * ArgumentParser is a utility class designed to parse and handle command-line arguments for applications.
 * It supports both positional arguments and named flags, as well as sub-commands with their own sets
 * of arguments. It also provides functionality to print help messages based on the defined arguments
 * and commands, and can validate argument values using custom validation functions.
 */
public class ArgumentParser extends Parser {
    //Storage

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
        storeCommandInMap(command.identifier(), command);
    }

    /* MAP STORAGE METHODS */
    private Command getCommandFromMap(String identifier) {
        if (!commands.containsKey(identifier)) {
            throw new IllegalArgumentException("Command with name '" + identifier + "' not found.");
        }
        return commands.get(identifier);
    }

    private void storeCommandInMap(String identifier, Command command) {
        if(commands.containsKey(identifier)) {
            throw new IllegalArgumentException("Value with name '" + identifier + "' already exists.");
        }
        commands.put(identifier, command);
    }

    /* HELP MESSAGE */

    /**
     * Prints all the arguments and commands attached to this Command object.
     *
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

    /**
     * Getter for a specific argument in a command.
     *
     * @param command The identifier of the command to retrieve the command from the map of Commands.
     * @param name The name of the Argument to retreive from the command's map of arguments.
     * @return The parsed value of the command line argument that is stored in the Command object.
     */
    public Object getParsedCommandArgument(String command, String name) {
        return getCommandFromMap(command).getParsedArgument(name);
    }
    /**
     * Getter for all the parsed arguments in a Command.
     *
     * @param command String representation of the unique identifier of the Command for retrieval in the map.
     * @return A map parsed Commands where the keys are the command's unique identifier.
     */
    public Map<String, Object> getParsedCommandArguments(String command) {
        return getCommandFromMap(command).getParsedArguments();
    }

    /**
     * Parses the input string to handle different types of command arguments or flags.
     * This method tokenizes the input based on spaces, validates,then routes
     * the command processing based on the type of argument or command detected.
     *
     * @param input The raw input string containing the command and its arguments.
     * @throws Exception If the first token does not match the expected command identifier, indicating that
     *                   the input is not intended for this parser, or if any other parsing error occurs.
     */
    public void parseArgs(String input) throws Exception {
        //Tokenizes
        List<String> tokens = new ArrayList<>();
        for (String token : Splitter.on(' ')
                .trimResults()
                .omitEmptyStrings()
                .split(input)) {
            tokens.add(token);
        }

        // Validate Identifier
        if(!(Objects.equals(tokens.getFirst(), identifier))) {
            throw new Exception("Incorrect Identifier in ArgumentParser");
        }
        tokens.removeFirst();

        if(!commands.isEmpty() && commands.containsKey(tokens.getFirst())) {
            Command command = getCommandFromMap(tokens.getFirst());
            command.parseArgs(tokens);
        } else if (tokens.getFirst() == "-h" || tokens.getFirst() == "--help") {
            printHelpMessage();
        } else {
            parse(tokens);
        }
    }
}

