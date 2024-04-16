package oop.project.cli;

import com.google.common.base.Splitter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a parser that parses and stores arguments and parsed values.
 */
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

    /**
     * Private constructor to prevent instantiation without required fields.
     */
    public Parser(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
        this.description = null;
    }

    /**
     * Constructs a oop.project.cli.Parser with a specified name and identifier.
     *
     * @param name The display name of the parser.
     * @param identifier The unique identifier for the parser.
     */
    public Parser(String name, String identifier, String description) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
    }

    /* GETTER METHODS */
    /**
     * Returns the name of the parser.
     * @return The name of the parser.
     */
    public String name() { return name; }
    /**
     * Returns the identifier of the parser.
     * @return The identifier of the parser.
     */
    public String identifier() { return identifier; }

    /* ARGUMENT METHOD */
    /**
     * Adds an argument to the parser's list of arguments.
     *
     * @param argument The argument to be added.
     */
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
    /**
     * Retrieves the parsed value of a specified argument by casting it to its declared type.
     * This method fetches the value from the internal map and casts it based on the type information
     * provided by the Argument object associated with the name.
     *
     * @param name The name of the argument whose parsed value is to be retrieved.
     * @return The parsed value of the argument, cast to its appropriate type.
     * @throws IllegalArgumentException If the argument name does not exist or the value is empty.
     */
    public Object getParsedArgument(String name) {
        Class type = getArgumentFromMap(name).type();
        Object value = getValueFromMap(name);
        return type.cast(value);
    }
    /**
     * Retrieves all arguments that have been parsed along with their corresponding values.
     * This method filters out any arguments that have not been set (i.e., their values are Optional.empty())
     * and returns a map of argument names to their parsed values.
     *
     * @return A map of argument names to their corresponding values, excluding any arguments that have not been set.
     */
    public Map<String, Object> getParsedArguments() {
        Map<String, Object> parsedArgs = values.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent()) // Filter out entries with Optional.empty()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));

        return parsedArgs;
    }

    private void flagged(List<String> tokens) throws Exception {
        if(tokens.size() % 2 != 0) {
            throw new ParseException("Unexpected Arguments");
        }

        Integer nameIndex = 0;
        Integer valueIndex = 1;
        for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
            String name = entry.getKey();
            Argument argument = entry.getValue();

            if (nameIndex >= tokens.size() || valueIndex >= tokens.size()) {
                if(argument.required() == Boolean.TRUE) {
                    throw new ParseException("Missing Required Argument -> " + name);
                }
                continue;
            }

            String tokenName = tokens.get(nameIndex).replace("--", "");
            String tokenValue = tokens.get(valueIndex);

            if (!arguments.containsKey(tokenName)) {
                throw new ParseException("Argument does not exist -> " + tokenName);
            } else if (Objects.equals(name, tokenName)) {
                // Validate and Store
                storeValueInMap(name, argument.validate(tokenValue));
                // Increase ptrs
                nameIndex = nameIndex + 2;
                valueIndex = valueIndex + 2;
            } else if (argument.required()) {
                throw new ParseException("Missing Required Argument -> " + name);
            }
        }
    }

    private void positional(List<String> tokens) throws Exception {
        if (tokens.size() != arguments.size()) {
            throw new ParseException("Mismatch between number of expected arguments and given arguments.");
        }

        Integer valueIndex = 0;
        for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
            if (valueIndex >= tokens.size()) {
                break;
            }

            String name = entry.getKey();
            Argument argument = entry.getValue();

            String tokenValue = tokens.get(valueIndex);

            storeValueInMap(name, argument.validate(tokenValue));
            valueIndex++;
        }
    }

    protected void parse(List<String> tokens) throws Exception {
        Boolean flagged = Boolean.FALSE;
        if(tokens.getFirst().contains("--")) {
            flagged = Boolean.TRUE;
        }

        if(flagged) {
            flagged(tokens);
        } else {
            positional(tokens);
        }

    }
}
