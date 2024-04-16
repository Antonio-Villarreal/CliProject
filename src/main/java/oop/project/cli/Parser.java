package oop.project.cli;

import com.google.common.base.Splitter;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Parser {
    //Required
    protected final String name;
    protected final String identifier;

    //Optional
    protected String description;

    //Storage
    protected Map<String, Argument> arguments = new LinkedHashMap<>();
    protected Map<String, Optional<Object>> values = new LinkedHashMap<>();

    /* CONSTRUCTORS */

    private Parser() {
        name = null;
        identifier = null;
    }

    public Parser(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
        this.description = null;
    }

    public Parser(String name, String identifier, String description) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
    }

    public String name() { return name; }

    /* ARGUMENT METHOD */

    public void addArgument(Argument argument) {
        storeArgumentInMap(argument.name(), argument);
    }

    /* MAP STORAGE METHODS */

    private Argument getArgumentFromMap(String name) {
        if (!arguments.containsKey(name)) {
            throw new IllegalArgumentException("Argument with name '" + name + "' not found.");
        }
        return arguments.get(name);
    }

    private Object getValueFromMap(String name) {
        if (!values.containsKey(name)) {
            throw new IllegalArgumentException("Value with name '" + name + "' not found.");
        } else if (values.get(name) == Optional.empty()) {
            throw new IllegalArgumentException("Value with name '" + name + "' found but was empty.");
        }
        return values.get(name);
    }

    private void storeArgumentInMap(String name, Argument argument) {
        if(arguments.containsKey(name)) {
            throw new IllegalArgumentException("Argument with name '" + name + "' already exists.");
        }
        values.put(name, Optional.empty());
        arguments.put(name, argument);
    }

    private void storeValueInMap(String name, Object value) {
        values.put(name, Optional.ofNullable(value));
    }

    /* PARSING METHODS */

    public Object getParsedArgument(String name) {
        Class type = getArgumentFromMap(name).type();
        Object value = getValueFromMap(name);
        return type.cast(value);
    }

    public Map<String, Object> getParsedArguments() {
        Map<String, Object> parsedArgs = values.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().orElse(null)));
        return parsedArgs;
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
            if(arg.required()){
                if(!argName.equals(flags.get(count).substring(2))) {
                    throw new Exception("Missing required argument.");
                }
            }
            if(argName.equals(flags.get(count).substring(2))){
                storeValueInMap(argName, arg.validate(positionalArguments.get(count)));
                count++;
            }
        }
    }

    private void handlePositional(List<String> positionalArguments) throws Exception {
        int count = 0;
        for(Map.Entry<String, Argument> argument : arguments.entrySet()){
            String argName = argument.getKey();
            Argument arg = argument.getValue();
            storeValueInMap(argName, arg.validate(positionalArguments.get(count)));
            count++;
        }
        if(count != positionalArguments.size()){
            throw new IllegalArgumentException("Incorrect number of arguments (not PosArg)"); // TODO Change the error message later
        }
    }
}
