package oop.project.cli;

import java.util.Scanner;

public class Main {

    /**
     * A default implementation of main that can be used to run scenarios.
     */
    public static void main(String[] args) {
        ArgumentParser argparse = new ArgumentParser("Addition", "add", "Performs addition");
        argparse.addArgument("left", Integer.class);
        argparse.addArgument("right", Integer.class);
        System.out.println(argparse.arguments);


//        var scanner = new Scanner(System.in);
//        while (true) {
//            var input = scanner.nextLine();
//            if (input.equals("exit")) {
//                break;
//            }
//            try {
//                var result = Scenarios.parse(input);
//                System.out.println(result);
//            } catch (Exception e) {
//                System.out.println("Unexpected exception: " + e.getClass().getName() + ", " + e.getMessage());
//            }
//        }
    }

}
