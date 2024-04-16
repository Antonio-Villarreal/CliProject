package oop.project.cli;

import com.google.common.base.Splitter;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class Command extends Parser {

    /* CONSTRUCTORS */

    public Command(String name, String identifier) {
        super(name, identifier);
    }

    public Command(String name, String identifier, String description) {
        super(name, identifier, description);
    }

    /* HELP MESSAGE */

    public String getMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append("\t").append(identifier).append("\t");
        for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
            Argument argument = entry.getValue();
            msg.append(argument.getMessage());
        }
        return msg.toString();
    }

    public void printHelpMessage() {
        System.out.println(name);
        System.out.println();
        if(description != null) {
            System.out.println(description);
            System.out.println();
        }
        System.out.println("Usage: " + identifier + " [ARGUMENTS] [COMMAND]");
        System.out.println();
        System.out.println("Arguments:");
        for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
            Argument argument = entry.getValue();
            System.out.println(argument.getMessage());
        }
        System.out.println();
        System.out.println();
        System.out.println("Optional Arguments:");
        System.out.println("\t--help\tMSG: Show the help message.");
    }

    public void parseArgs(List<String> tokens) throws Exception {
        if(!(Objects.equals(tokens.getFirst(), identifier))) {
            throw new Exception("Incorrect Identifier in Command");
        }
        tokens.removeFirst();

        if (Objects.equals(tokens.getFirst(), "-h") || Objects.equals(tokens.getFirst(), "--help")) {
            printHelpMessage();
        } else {
            parse(tokens);
        }
    }
}
