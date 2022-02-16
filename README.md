# Overview

This software acts as an interpreter for the Brainfudge esoteric programming language. The software is written in Java, and therefore Java is required to be installed to use (see Development Environment header).

Because Brainfudge is an alternate name for the original language, Brain****, some examples, or listed references, may contain vulgar language.

This is my first application written in Java, and I used this as a way to learn my way around the Java language.

Featured in this interpreter are the following:
* A modular interpreter that can easily be integrated into other Java projects
* A full-featured REPL for the Brainfudge language.
* A BF file interpreter

## Use

To use the BF interpreter, the program must first be built from the source code.

Once the application is built, the program is meant to be used from the command line. 

Use the following command to run the program:

`java com.trevinj.fudgerunner.Main`

If no arguments are passed, the program will display a short help message.

### Allowed arguments:
* `-h` Display the help menu
* `-r` Launch the BF REPL (see below for instructions on using the REPL)
* `-f filename` Interpret and execute the BF code from the specified file

Any argument passed that is not listed above will result in an error, and the program will exit.

### Using the REPL

The REPL allows BF code to be executed line by line. You input a string of Brainfudge instructions and the program will execute them. When using the REPL, there are a few things to keep in mind:
* Each new line is executed as a continuation of the instructions passed before it.
    * This means that the memory of the REPL is persistent until the interpreter is reset (see below for details on when and how the interpreter resets)
* Before the REPL interprets a line of instructions, it runs a check to see if all brackets match.
    * If the brackets do not match, this will result in an interpreter error. (See below for how the interpreter handles errors)
    * The brackets must match because of the nature of REPLs. Each line of instructions is immediately executed, and if there are mismatched brackets, it is impossible to run the code.
* Interpreter errors result in the error being printed to the screen, and the interpreter resetting.
* Resetting the interpreter means that every stored instruction is cleared, the code and cell pointers are reset, and the cells are cleared. (See below for manually resetting the interpreter)

REPL special commands:
* `show` -- Display the current cell pointer, and display the value in that cell. To see the ASCII representation of that value, use the BF instruction: `.`
* `reset` -- Manually reset the interpreter. The REPL instruction number does not reset.
* `exit` -- Exit the REPL.

## Example BF files

Example Brainfudge files can be found in the BFExamples directory. Sources are listed at the top of the file. The included examples are described here:

* HelloWorld.b - Prints `Hello World!`. Contains documentation on how the program works.
* HelloWorldCondensed.b - Also prints `Hello World!`, except it is condensed to one line, and has no documentation. Both files are treated exactly the same by the interpreter.
* Check out the MoreExamples.txt file for a list of some more fun and complex BF programs.

[Software Demo Video](https://youtu.be/0OMaUiV9wek)

# Development Environment

* IntelliJ IDEA Community Edition 2021.3.1
* Java 16 (Required to use software)

# Useful Websites

* [W3schools Java](https://www.w3schools.com/java/default.asp)
* [GeeksforGeeks](https://www.geeksforgeeks.org/)
* [Brainfudge on Esolang](https://esolangs.org/wiki/Brainfudge)

# Future Work

* Improve the REPL design and fix newline bugs when using many `.` and `,` instructions.
* Support debugging features for running from a file (such as break points and instruciton-by-instruction execution)
* Support SMBF (Self-Modifying Brainfudge) interpretation