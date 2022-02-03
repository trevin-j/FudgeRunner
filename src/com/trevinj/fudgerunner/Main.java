package com.trevinj.fudgerunner;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // At least one arg is required to run.
        // If args[0] is -f, a second arg is required. The second arg is the file to run.
        // Possible flags are -h, -r, and -f
        // -h displays a short help menu
        // -r opens the BrainRunner BrainFudge REPL
        // -f opens and runs the passed file

        // if no flags are passed, default to -h
        String firstArg;
        if (args.length == 0) {
            firstArg = "-h";
        }
        else {
            firstArg = args[0];
        }

        // -h handling
        if (firstArg.equals("-h")) {
            displayHelp();
        }
        else if (firstArg.equals("-r")) {
            openRepl();
        }
        else if (firstArg.equals("-f")) {
            if (args.length > 1) {
                runFile(args[1]);
            }
        }
        else {
            System.out.println("Error: unrecognized argument '" + firstArg + "'.");
            return;
        }
    }

    /**
     * displayHelp prints the help menu to the screen, then exits.
     */
    private static void displayHelp() {
        System.out.println("Command        Use");
        System.out.println("-----------------------------------------");
        System.out.println("-h             Display help menu.");
        System.out.println("-r             Start the BrainRunner BrainFudge REPL.");
        System.out.println("-f filename    Open and execute the BrainFudge code in the specified file.");
        return;
    }

    /**
     * openRepl opens the BF REPL
     */
    private static void openRepl() {
        Interpreter replInterpreter = new Interpreter();
        Scanner commandScanner = new Scanner(System.in);
        int commandNumber = 0;

        System.out.println("[REPL]: BrainRunner Brainfudge REPL activated.");

        // Forever run the repl until the user specifies to exit.
        // If an error occurs while using the repl, the interpreter data fully resets.
        while (true) {
            System.out.println();
            System.out.print("[" + commandNumber + "]: ");
            String commandIn = commandScanner.next();

            // Check if the command is one of the REPL-specific commands
            // show command prints the current status of the interpreter
            if (commandIn.equals("show")) {
                System.out.print("> ");
                replInterpreter.show();
            }
            // exit command exits the REPL
            else if (commandIn.equals("exit")) {
                break;
            }
            // reset the interpreter
            else if (commandIn.equals("reset")) {
                replInterpreter.reset();
                System.out.println("Interpreter memory reset.");
            }
            else {
                // EVERYTHING else is considered a comment or valid instructions
                // Make sure brackets match in each line, or the REPL will error and the interpreter will reset.
                replInterpreter.addInstructions(commandIn);
                replInterpreter.runBF(false);
                commandNumber++;
            }
        }
    }

    /**
     * runFile opens and executes the bf code in the specified file
     * @param fileName the name of the file to run.
     */
    private static void runFile(String fileName) {

    }
}
