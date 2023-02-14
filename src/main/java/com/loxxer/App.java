package com.loxxer;

import com.loxxer.errors.LoxxerException;
import com.loxxer.scanner.LoxxerScanner;

/**
 * Main Application Class
 * This takes a source file written in Lox and interprets it line by line
 *
 * @param filepath Absolute path to the Lox source file
 * @return None
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
            LoxxerScanner scanner = new LoxxerScanner();
            Loxxer loxxer = new Loxxer(scanner);

            loxxer.run(filepath);
        } catch (LoxxerException e) {
            System.out.println(e.getMessage());
        }
    }
}