package com.loxxer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import com.loxxer.error.ErrorHandler;
import com.loxxer.lexical.LexicalError;
import com.loxxer.lexical.LexicalScanner;
import com.loxxer.lexical.LexicalToken;
import com.loxxer.parser.Parser;
import com.loxxer.parser.ParsingError;
import com.loxxer.parser.RuntimeError;
import com.loxxer.parser.classes.statements.Stmt;
import com.loxxer.visitor.StmtVisitor;

/**
 * Main class to run the interpreter
 */
public class Loxxer {
    // Show the input prompt
    private void prompt() {
        System.out.print("> ");
    }

    public static void main(String[] args) throws IOException {

        // Check if some file path has been provided, if not then start in CLI mode
        if (args.length > 0) {
            if (args.length > 1) {
                System.out.println("Error: Too many arguments");
            } else {
                String filepath = args[0];

                try {
                    ErrorHandler errorHandler = new ErrorHandler(false);
                    String source = new String(Files.readAllBytes(Paths.get(filepath)));
                    LexicalScanner lexicalScanner = new LexicalScanner(source, errorHandler);

                    // Send file for scanning
                    List<LexicalToken> tokens = lexicalScanner.scan(source);

                    // Parse to form the Syntax Tree
                    Parser parser = new Parser(tokens, errorHandler);
                    List<Stmt> program = parser.parse();

                    StmtVisitor stmtVisitor = new StmtVisitor(errorHandler);
                    for (Stmt statement : program) {
                        statement.accept(stmtVisitor);
                    }

                } catch (IOException e) {
                    System.out.println("Error: File not found");
                } catch (LexicalError e) {
                    System.out.println(e.getErrorMessage());
                    System.exit(65);
                } catch (ParsingError e) {
                    System.out.println(e.getErrorMessage());
                    System.exit(65);
                } catch (RuntimeError e) {
                    System.out.println(e.getErrorMessage());
                    System.exit(70);
                }
            }
        } else {
            System.out.println("Running lox in interpreter mode. Type exit() to exit the interpreter.");
            Loxxer loxxer = new Loxxer();
            Scanner input = new Scanner(System.in);

            // Run the interpreter prompt
            while (true) {
                loxxer.prompt();
                String source = input.nextLine();

                if (source.equals("exit()")) {
                    input.close();
                    return;
                } else {
                    try {
                        ErrorHandler errorHandler = new ErrorHandler(false);
                        LexicalScanner lexicalScanner = new LexicalScanner(source, errorHandler);

                        // Send file for scanning
                        List<LexicalToken> tokens = lexicalScanner.scan(source);

                        // Parse to form the Syntax Tree
                        Parser parser = new Parser(tokens, errorHandler);
                    } catch (LexicalError e) {
                        System.out.println(e.getErrorMessage());
                    } catch (ParsingError e) {
                        System.out.println(e.getErrorMessage());
                    } catch (RuntimeError e) {
                        System.out.println(e.getErrorMessage());
                    }
                }
            }
        }
    }
}