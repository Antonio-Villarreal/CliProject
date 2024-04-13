package oop.project.cli;

import java.util.Map;
import java.util.LinkedHashMap;

public class ArgumentParser {
    String name;
    String identifier;
    String description;

    Map<String, Argument> arguments = new LinkedHashMap<>();
    Map<String, Object> values = new LinkedHashMap<>();

    private ArgumentParser() {}

    public ArgumentParser(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
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

    private Argument getArgument(String name) {
        if (arguments.containsKey(name)) {
            throw new IllegalArgumentException("Argument with name '" + name + "' not found.");
        }
        return arguments.get(name);
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

    private void storeValue(String name, Object value) {
        if(values.containsKey(name)) {
            throw new IllegalArgumentException("Value with name '" + name + "' already exists.");
        }
        values.put(name, value);
    }

//    public ArgumentParser addCommand() {}
//
//    public void parseArgs() {}
//
//    public Map<String, Object> getArgs() {}
//
//    public Object getArg() {}

}
