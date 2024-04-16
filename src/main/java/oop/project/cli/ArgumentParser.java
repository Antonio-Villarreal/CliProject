package oop.project.cli;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

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
        storeCommandInMap(command.name(), command);
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
                System.out.println(argument.printHelp());
            }
        }
        if (!commands.isEmpty()) {
            System.out.println();
            System.out.println("Commands:");
            for (Map.Entry<String, Command> entry : commands.entrySet()) {
                Command command = entry.getValue();
                command.printCompactHelpMessage();
            }
        }
        System.out.println();
        System.out.println("Optional Arguments:");
        System.out.println("\t--help\tMSG: Show the help message.");
    }
}
