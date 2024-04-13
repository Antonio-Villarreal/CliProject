package oop.project.cli;

import java.util.Map;
import java.util.LinkedHashMap;

public class ArgumentParser {
    String name;
    String identifier;
    String description;

    Map<String, Argument> arguments = new LinkedHashMap<>();

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
        arguments.put(name, newArgument);
    }

    public void updateArgumentHelpMsg(String name, String description) {
        getArgument(name).setHelpMsg(description);
    }

    public void updateArgumentValidator(String name, ValidationFunction<?> validationFunction) {
        getArgument(name).setValidationFunc(validationFunction);
    }

    public void updateArgumentRequired(String name, Boolean required) {
        getArgument(name).setRequired(required);
    }

    private Argument getArgument(String name) {
        Argument currArgument = arguments.get(name);
        if (currArgument == null) {
            throw new IllegalArgumentException("Argument with name '" + name + "' not found.");
        }
        return currArgument;
    }

//    public ArgumentParser addCommand() {}
//
//    public void parseArgs() {}
//
//    public Map<String, Object> getArgs() {}
//
//    public Object getArg() {}

}
