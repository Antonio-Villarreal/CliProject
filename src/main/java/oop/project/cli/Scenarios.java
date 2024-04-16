package oop.project.cli;

import java.time.LocalDate;
import java.util.Map;

public class Scenarios {

    /**
     * Parses and returns the arguments of a command (one of the scenarios
     * below) into a Map of names to values. This method is provided as a
     * starting point that works for most groups, but depending on your command
     * structure and requirements you may need to make changes to adapt it to
     * your needs - use whatever is convenient for your design.
     */
    public static Map<String, Object> parse(String command) throws Exception {
        //This assumes commands follow a similar structure to unix commands,
        //e.g. `command [arguments...]`. If your project uses a different
        //structure, e.g. Lisp syntax like `(command [arguments...])`, you may
        //need to adjust this a bit to work as expected.
        var split = command.split(" ", 2);
        var base = split[0];
        var arguments = split.length == 2 ? split[1] : "";
        return switch (base) {
            case "add" -> add(arguments);
            case "sub" -> sub(arguments);
            case "sqrt" -> sqrt(arguments);
//            case "calc" -> calc(arguments);
            case "date" -> date(arguments);
            default -> throw new IllegalArgumentException("Unknown command.");
        };
    }

    /**
     * Takes two positional arguments:
     *  - {@code left: <your integer type>}
     *  - {@code right: <your integer type>}
     */
    private static Map<String, Object> add(String arguments) throws Exception {
        ArgumentParser argparse = new ArgumentParser("Addition", "add", "Performs addition");
        argparse.addArgument(new Argument.Builder<>("left", Integer.class)
                .required(Boolean.TRUE)
                .build());
        argparse.addArgument(new Argument.Builder<>("right", Integer.class)
                .required(Boolean.TRUE)
                .build());
        argparse.parseArgs(arguments);
        return argparse.getParsedArguments();
    }

    /**
     * Takes two <em>named</em> arguments:
     *  - {@code left: <your decimal type>} (optional)
     *     - If your project supports default arguments, you could also parse
     *       this as a non-optional decimal value using a default of 0.0.
     *  - {@code right: <your decimal type>} (required)
     */
    static Map<String, Object> sub(String arguments) throws Exception {
        //TODO: Parse arguments and extract values.
        ArgumentParser argparse = new ArgumentParser("Subtract", "sub", "Performs subtraction");
        argparse.addArgument(new Argument.Builder<>("left", Double.class)
                .required(Boolean.FALSE)
                .build());
        argparse.addArgument(new Argument.Builder<>("right", Double.class)
                .required(Boolean.TRUE)
                .build());
        argparse.parseArgs(arguments);
        return argparse.getParsedArguments();
    }

    /**
     * Takes one positional argument:
     *  - {@code number: <your integer type>} where {@code number >= 0}
     */
    static Map<String, Object> sqrt(String arguments) throws Exception {
        //TODO: Parse arguments and extract values.
        ArgumentParser argparse = new ArgumentParser("Square Root", "sqrt", "Performs Square Root");
        ValidationFunction<Integer> nonNegativeValidator = value -> value >= 0;
        argparse.addArgument(new Argument.Builder<>("number", Integer.class)
                .required(Boolean.TRUE)
                .validationFunction(nonNegativeValidator)
                .build());
        argparse.parseArgs(arguments);
        return argparse.getParsedArguments();
    }


    /**
     * Takes one positional argument:
     *  - {@code subcommand: "add" | "div" | "sqrt" }, aka one of these values.
     *     - Note: Not all projects support subcommands, but if yours does you
     *       may want to take advantage of this scenario for that.
     */
//    static Map<String, Object> calc(String arguments) throws Exception {
//        //TODO: Parse arguments and extract values.
//        ArgumentParser argparse = new ArgumentParser("Calculator", "calc", "Performs add, div, and sqrt.");
//
//        Command addCommand = new Command("Addition", "add");
//        addCommand.addArgument(new Argument.Builder<>("left", Integer.class)
//                .required(Boolean.TRUE)
//                .build());
//        addCommand.addArgument(new Argument.Builder<>("right", Integer.class)
//                .required(Boolean.TRUE)
//                .build());
//        argparse.addCommand(addCommand);
//
//        Command divCommand = new Command("Division", "div");
//        divCommand.addArgument(new Argument.Builder<>("numerator", Float.class)
//                .required(Boolean.FALSE)
//                .build());
//        divCommand.addArgument(new Argument.Builder<>("denominator", Float.class)
//                .required(Boolean.TRUE)
//                .build());
//        argparse.addCommand(divCommand);
//
//        Command sqrtCommand = new Command("Square Root", "sqrt");
//        ValidationFunction<Integer> nonNegativeValidator = value -> value >= 0;
//        sqrtCommand.addArgument(new Argument.Builder<>("number", Integer.class)
//                .required(Boolean.TRUE)
//                .validationFunction(nonNegativeValidator)
//                .build());
//        argparse.addCommand(sqrtCommand);
//        // GET SUBCOMMAND
//        //return Map.of("subcommand", subcommand);
//    }

    /**
     * Takes one positional argument:
     *  - {@code date: Date}, a custom type representing a {@code LocalDate}
     *    object (say at least yyyy-mm-dd, or whatever you prefer).
     *     - Note: Consider this a type that CANNOT be supported by your library
     *       out of the box and requires a custom type to be defined.
     */
    static Map<String, Object> date(String arguments) throws Exception {
        //TODO: Parse arguments and extract values.
        ArgumentParser argparse = new ArgumentParser("Calendar", "date", "Performs String to Date Conversion");
        argparse.addArgument(new Argument.Builder<>("date", LocalDate.class)
                .required(Boolean.TRUE)
                .customTypeConversionMethod("parse")
                .build());
        argparse.parseArgs(arguments);
        return argparse.getParsedArguments();
    }

    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a
    //good place to test/showcase your functionality in context.

//    static Map<String, Object> unrecognizedArgument(String arguments) throws Exception {
//        //TODO: Parse arguments and extract values.
//        ArgumentParser argparse = new ArgumentParser("Addition", "add", "Adds two numbers together.");
//        argparse.addArgument(new Argument.Builder<>("left", Integer.class)
//                .required(Boolean.TRUE)
//                .build());
//        argparse.addArgument(new Argument.Builder<>("right", Integer.class)
//                .required(Boolean.TRUE)
//                .build());
//        argparse.parseArgs(arguments);
//        return argparse.getParsedArguments();
//    }
}
