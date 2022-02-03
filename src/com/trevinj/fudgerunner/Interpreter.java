package com.trevinj.fudgerunner;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * The Interpreter class is used for interpreting Brainfudge code.
 * It is passed the code and automatically executes all the instructions.
 * Optionally, instructions can be executed one by one, for example for debugging purposes.
 */
public class Interpreter {
    // Fields

    /**
     * Scanner for input
     */
    private final Scanner scanIn = new Scanner(System.in);

    // Fields used for interpreting the data
    /**
     * cellPtr points to the cell that the program is pointing to
     */
    private int cellPtr;
    /**
     * codePtr points to the current instruction in the program
     */
    private int codePtr;
    /**
     * instructions is an ArrayList containing all the instructions stored as characters
     */
    private ArrayList<Character> instructions = new ArrayList<>();
    /**
     * cells is an ArrayList containing all the modifiable cells
     */
    private ArrayList<Integer> cells;
    /**
     * This stack helps with looping properly
     */
    private Stack<Integer> leftLoopCells;

    /**
     * The class constructor initializes the Interpreter.
     */
    public Interpreter() {
        reset();
    }

    /**
     * Set the instructions to be the passed bf code
     * @param instructions This is the bf instructions stored as a String
     */
    public void setInstructions(String instructions) {
        this.instructions = strToArrayList(instructions);
    }

    /**
     * Overload the runBF method so that it has a default value of true for doReset
     */
    public void runBF() {
        runBF(true);
    }

    /**
     * Interpret the BF code, and use console for IO.
     */
    public void runBF(boolean doReset) {
        if (!bracketsAreMatched()) {
            reset();
            return;
        }

        // Reset some variables for execution
        if (doReset)
            reset();

        // While it is still reading instructions, process the next instruction.
        // Ignore any and all characters that are not one of the 8 instructions.
        while (codePtr < instructions.size()) {
            if (!processNextInstruction()) {
                reset();
                break;
            }
            codePtr++;
        }

    }

    public void addInstructions(String instructionsIn) {
        ArrayList<Character> splitInstructions = strToArrayList(instructionsIn);
        for (Character instruction: splitInstructions) {
            instructions.add(instruction);
        }
    }

    /**
     * Reset the variables that need to be reset before starting to execute code
     * This method is public so that the interpreter can be manually reset.
     */
    public void reset() {
        cellPtr = 0;
        codePtr = 0;
        cells = new ArrayList<>();
        cells.add(0);
        leftLoopCells = new Stack<>();
        instructions = new ArrayList<>();
    }

    /**
     * This processes the next instruction that is stored in instructions.
     * It also increments the codePtr to keep track of where it is in the program.
     * @return true if no errors occur, false otherwise.
     */
    public boolean processNextInstruction() {
        Character instruction = instructions.get(codePtr);
        String allowedInstructions = "[]<>,.+-";

        // If the instruction is not a valid, i.e. if it is a comment, ignore it.
        if (!allowedInstructions.contains(instruction.toString())) {
            return true;
        }

        // First make sure that cells is long enough to prevent index errors
        while (cells.size() < cellPtr) {
            cells.add(0);
        }

        // Process the instruction
        // -----------------------

        // increment/decrement
        if (instruction.equals('+')) {
            cells.set(cellPtr, cells.get(cellPtr) + 1);
            return true;
        }
        if (instruction.equals('-')) {
            cells.set(cellPtr, cells.get(cellPtr) - 1);
            return true;
        }

        // pointer movement
        if (instruction.equals('>')) {
            cellPtr++;
            if (cellPtr >= cells.size()) {
                cells.add(0);
            }
            return true;
        }
        if (instruction.equals('<')) {
            cellPtr--;
            if (cellPtr < 0) {
                System.out.println("Error: cell pointer out of bounds. (instruction " + (codePtr + 1) + ")");
                return false;
            }
            return true;
        }

        // IO
        if (instruction.equals('.')) {
            // Java primitives are a confusing jumble of crap.
            // Integers cannot be cast to their ascii char value, but ints can.
            // So first, implicitly convert the value at cellPtr from Integer to int.
            int cellInt = cells.get(cellPtr);
            // Then print it as its ascii value (by casting to char)
            System.out.print((char)cellInt);
            return true;
        }
        if (instruction.equals(',')) {
            // Regardless of how many chars user types, only first one is actually used.
            cells.set(cellPtr, (int)(scanIn.next().charAt(0)));
            return true;
        }

        // Loops
        if (instruction.equals('[')) {
            if (cells.get(cellPtr) != 0) {
                leftLoopCells.push(codePtr);
            }
            else {
                // If the cell is 0, skip to corresponding close bracket.
                // bracketCount is equal to open brackets - close brackets.
                // It knows it found the corresponding close bracket if bracketCount is 0 when it hits close bracket.
                int bracketCount = 0;
                while (true) {
                    codePtr++;
                    if (instructions.get(codePtr).equals('['))
                        bracketCount++;
                    else if (instructions.get(codePtr).equals(']')) {
                        if (bracketCount == 0)
                            break;
                        else
                            bracketCount--;
                    }
                }
            }
            return true;
        }
        if (instruction.equals(']')) {
            // We want it to be set to where the '[' is, but it gets incremented after this method returns.
            // So, subtract one to counteract that.
            codePtr = leftLoopCells.pop() - 1;
            return true;
        }

        // If it gets to this point, something unexpected happened.
        System.out.println("Error: unexpected result at instruction " + (codePtr + 1) + ".");
        return false;
    }

    /**
     * Print out the interpreter data to the screen.
     */
    public void show() {
        System.out.println("Cell #: " + cellPtr + "; Cell value: " + cells.get(cellPtr) + ";");
    }

    /**
     * Get an ArrayList of all the characters in a String.
     * @param strIn The String to retrieve the Characters from.
     * @return ArrayList holding each character from the string
     */
    private static ArrayList<Character> strToArrayList(String strIn) {
        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i < strIn.length(); i++){
            chars.add(strIn.charAt(i));
        }
        return chars;
    }

    /**
     * Check if the brackets in the instructions all have a match.
     * @return true if they all match, false otherwise.
     */
    private boolean bracketsAreMatched() {
        int bracketCount = 0;

        for (int i = 0; i < instructions.size(); i++) {
            Character instruction = instructions.get(i);
            if (instruction.equals('['))
                bracketCount++;
            else if (instruction.equals(']'))
                bracketCount--;
            if (bracketCount < 0) {
                System.out.println("Error: mismatched ']' at instruction " + (i + 1) + ".");
                return false;
            }
        }

        if (bracketCount == 0) {
            return true;
        }
        System.out.println("Error: mismatched '['.");
        return false;
    }

}
