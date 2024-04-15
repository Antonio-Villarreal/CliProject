package oop.project.cli;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
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
        Argument newArgument = new Argument(name, type);
        storeArgument(name, newArgument);
    }

    public void updateArgumentHelpMsg(String name, String description) {
        getArgument(name).setHelpMsg(description);
    }

    public void updateArgumentValidationFunc(String name, ValidationFunction<?> validationFunction) {
        getArgument(name).setValidationFunc(validationFunction);
    }

    public void updateCustomTypeConversionMethod(String name, String customTypeConversionMethod) {
        getArgument(name).setCustomTypeConversionMethod(customTypeConversionMethod);
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
        if (!arguments.containsKey(name)) {
            throw new IllegalArgumentException("Argument with name '" + name + "' not found.");
        }
        return arguments.get(name);
    }

    private Command getCommand(String identifier) {
        if (!commands.containsKey(identifier)) {
            throw new IllegalArgumentException("Command with name '" + identifier + "' not found.");
        }
        return commands.get(identifier);
    }

    private Object getValue(String name) {
        if (!values.containsKey(name)) {
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

    private void handleFlagged(List<String> flags, List<String> positionalArguments) throws Exception {
        // Check the exception
        if(flags.size() != positionalArguments.size()){
            throw new IllegalArgumentException("Flags number should match with Arguments number"); //TODO Change the error message later
        }

        // Validate flags and positional arguments
        int count = 0;
        for( Map.Entry<String, Argument> argument : arguments.entrySet()){
            String argName = argument.getKey();
            Argument arg = argument.getValue();
            if(arg.required){
               if(!argName.equals(flags.get(count).substring(2))) {
                   throw new Exception("Missing required argument.");
               }
            }
            if(argName.equals(flags.get(count).substring(2))){
                values.put(argName, arg.validate(positionalArguments.get(count)));
                count++;
            }
        }
    }

    private void handlePositional(List<String> positionalArguments) throws Exception {
        int count = 0;
        for(Map.Entry<String, Argument> argument : arguments.entrySet()){
            String argName = argument.getKey();
            Argument arg = argument.getValue();
            values.put(argName, arg.validate(positionalArguments.get(count)));
            count++;
        }
        if(count != positionalArguments.size()){
            throw new IllegalArgumentException("Incorrect number of arguments (not PosArg)"); // TODO Change the error message later
        }
    }

    public void parseArgs(String input) throws Exception {

        Iterable<String> tokens = Splitter.on(' ')
                .trimResults()
                .omitEmptyStrings()
                .split(input);

        boolean isFirstToken = true;
        String command = null;
        List<String> flags = new ArrayList<>();
        List<String> positionalArguments = new ArrayList<>();

        //
        // TODO Scenarios.java's parse method removes the first token for us which is usually the root command ('git', 'cd', 'mv', etc.) so we don't have to check for it
        //
        for (String token : tokens) {
//            if (isFirstToken) {
//                command = token;
//                isFirstToken = false;
//            } else
                if (token.startsWith("--")) {
                flags.add(token);
            } else {
                positionalArguments.add(token);
            }
        }

        // Process flags
        if(!flags.isEmpty()){
            handleFlagged(flags, positionalArguments);
        }

        //process positional
        if(!positionalArguments.isEmpty()){
            handlePositional(positionalArguments);
        }

    }

    public <T> Object getArg(String name) {
        Class<T> type = getArgument(name).type;
        Object value = getValue(name);
        return type.cast(value);
    }


}
