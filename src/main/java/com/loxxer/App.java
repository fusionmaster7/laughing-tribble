package com.loxxer;

import com.loxxer.exceptions.LoxxerException;

/**
 * Main Application Class
 * This takes a source file written in Lox and interprets it line by line
 */
public class App {
    public static void main(String[] args) {
        // Check if filepath has been provided as args or not
        if (args.length <= 0) {
            System.out.println("Source file path not specified");
            System.exit(64);
        }

        try {
            // Run the interpreter using the given filepath
            String filepath = args[0];
            Loxxer.run(filepath);
        } catch (LoxxerException e) {
            System.out.println(e.getMessage());
        }
    }
}