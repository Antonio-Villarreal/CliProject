package oop.project.cli;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.Map;
import java.util.LinkedHashMap;

public class ArgumentParser {
    String name;
    String identifier;
    String description;

    Map<String, Argument> arguments = new LinkedHashMap<>();
    Map<String, Command> commands = new LinkedHashMap<>();
    Map<String, Object> values = new LinkedHashMap<>();

    private ArgumentParser() {}

    public ArgumentParser(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
        this.description = null;
    }

    public ArgumentParser(String name, String identifier, String description) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
    }

    public void addArgument(String name, Class<?> type) {
        var newArgument = new Argument(name, type);
        storeArgument(name, newArgument);
    }

    public void updateArgumentHelpMsg(String name, String description) {
        getArgument(name).setHelpMsg(description);
    }

    public void updateArgumentValidationFunc(String name, ValidationFunction<?> validationFunction) {
        getArgument(name).setValidationFunc(validationFunction);
    }

    public void updateArgumentRequired(String name, Boolean required) {
        getArgument(name).setRequired(required);
    }

    public Command createCommand(String name, String identifier) {
        Command newCommand = new Command(name, identifier);
        storeCommand(identifier, newCommand);
        return newCommand;
    }

    public Command createCommand(String name, String identifier, String description) {
        Command newCommand = new Command(name, identifier, description);
        storeCommand(identifier, newCommand);
        return newCommand;
    }

    public Map<String, Object> getArgs() {
        return values;
    }

    public Object getArg(String name) {
        return getValue(name);
    }

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

    private Argument getArgument(String name) {
        if (arguments.containsKey(name)) {
            throw new IllegalArgumentException("Argument with name '" + name + "' not found.");
        }
        return arguments.get(name);
    }

    private Command getCommand(String identifier) {
        if (commands.containsKey(identifier)) {
            throw new IllegalArgumentException("Argument with name '" + identifier + "' not found.");
        }
        return commands.get(identifier);
    }

    private Object getValue(String name) {
        if (values.containsKey(name)) {
            throw new IllegalArgumentException("Value with name '" + name + "' not found.");
        }
        return values.get(name);
    }

    private void storeArgument(String name, Argument argument) {
        if(arguments.containsKey(name)) {
            throw new IllegalArgumentException("Argument with name '" + name + "' already exists.");
        }
        arguments.put(name, argument);
    }

    private void storeCommand(String identifier, Command command) {
        if(commands.containsKey(identifier)) {
            throw new IllegalArgumentException("Value with name '" + identifier + "' already exists.");
        }
        commands.put(identifier, command);
    }

    private void storeValue(String name, Object value) {
        if(values.containsKey(name)) {
            throw new IllegalArgumentException("Value with name '" + name + "' already exists.");
        }
        values.put(name, value);
    }


}
