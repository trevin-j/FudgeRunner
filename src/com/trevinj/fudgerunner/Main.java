package com.trevinj.fudgerunner;

import java.io.File;
import java.io.FileNotFoundException;
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
        switch (firstArg) {
            case "-h":
                displayHelp();
                break;
            case "-r":
                openRepl();
                break;
            case "-f":
                if (args.length > 1) {
                    runFile(args[1]);
                }
                break;
            default:
                System.out.println("Error: unrecognized argument '" + firstArg + "'.");
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
        label:
        while (true) {
            System.out.println();
            System.out.print("[" + commandNumber + "]: ");
            String commandIn = commandScanner.next();

            // Check if the command is one of the REPL-specific commands
            // show command prints the current status of the interpreter
            commandNumber++;
            switch (commandIn) {
                case "show":
                    System.out.print("> ");
                    replInterpreter.show();
                    break;
                // exit command exits the REPL
                case "exit":
                    break label;

                // reset the interpreter
                case "reset":
                    replInterpreter.reset();
                    System.out.println("Interpreter memory reset.");
                    break;
                default:
                    // EVERYTHING else is considered a comment or valid instructions
                    // Make sure brackets match in each line, or the REPL will error and the interpreter will reset.
                    replInterpreter.addInstructions(commandIn);
                    replInterpreter.runBF();
                    break;
            }
        }
    }

    /**
     * runFile opens and executes the bf code in the specified file
     * @param fileName the name of the file to run.
     */
    private static void runFile(String fileName) {
        String code = getBFFromFile(fileName);

        // Exit if an error happens
        if (code == null)
            return;

        Interpreter fileInterpreter = new Interpreter();

        fileInterpreter.setInstructions(code);
        fileInterpreter.runBF();
    }

    /**
     * Get BF code from a file.
     * @param fileName The name of the bf file
     * @return the code as a String, or null if an error occurs
     */
    private static String getBFFromFile(String fileName) {
        // Open file if it exists. Print error if the file doesn't exist.
        try {
            StringBuilder code = new StringBuilder();
            File codeFile = new File(fileName);
            Scanner fileReader = new Scanner(codeFile);
            while (fileReader.hasNextLine()) {
                code.append(fileReader.nextLine());
            }
            return code.toString();

        } catch (FileNotFoundException e) {
            System.out.println("Error: the specified file does not exist.");
            return null;
        }
    }
}
