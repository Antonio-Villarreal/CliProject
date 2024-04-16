package oop.project.cli;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import com.google.common.base.Splitter;

import java.util.*;

public class ArgumentParser extends Parser {
    //Storage
    protected Map<String, Command> commands = new LinkedHashMap<>();

    /* CONSTRUCTORS */

    public ArgumentParser(String name, String identifier) {
        super(name, identifier);
    }

    public ArgumentParser(String name, String identifier, String description) {
        super(name, identifier, description);
    }

    /* COMMAND METHOD */

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

    public Object getParsedCommandArgument(String command, String name) {
        return getCommandFromMap(command).getParsedArgument(name);
    }

    public Map<String, Object> getParsedCommandArguments(String command) {
        return getCommandFromMap(command).getParsedArguments();
    }

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
