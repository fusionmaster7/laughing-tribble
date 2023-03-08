package com.loxxer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import com.loxxer.lexical.LexicalScanner;
import com.loxxer.lexical.LexicalToken;

/**
 * Main class to run the interpreter
 */
public class Loxxer {
    public static void main(String[] args) throws IOException {

        // Check if some file path has been provided, if not then start in CLI mode
        if (args.length > 0) {
            if (args.length > 1) {
                System.out.println("Error: Too many arguments");
            } else {
                String filepath = args[0];

                try {
                    String source = new String(Files.readAllBytes(Paths.get(filepath)));
                    LexicalScanner lexicalScanner = new LexicalScanner(source);

                    // Send file for scanning
                    List<LexicalToken> tokens = lexicalScanner.scan(source);

                    for (LexicalToken token : tokens) {
                        System.out.println(token.toString());
                    }
                } catch (IOException e) {
                    System.out.println("Error: File not found");
                }
            }
        } else {
            System.out.println("Running lox in interpreter mode");
            // Run the interpreter prompt
        }
    }
}