package com.loxxer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.loxxer.errors.LoxxerException;
import com.loxxer.scanner.LoxxerScanner;
import com.loxxer.tokens.LoxxerToken;

/**
 * Actual class which runs the interpreter
 *
 * @param filepath Absolute path to the Lox source file
 */
public class Loxxer {
    /**
     * The Scanner Entity
     */
    private LoxxerScanner scanner;

    public Loxxer(LoxxerScanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Runs the Lox interpreter
     *
     * @params filepath The absolute path to the Lox source code file
     */
    public void run(String filepath) throws LoxxerException {
        try {
            FileReader fileReader = new FileReader(filepath);
            List<LoxxerToken> tokenList = this.scanner.scan(fileReader);

            for (LoxxerToken token : tokenList) {
                System.out.println(token.toString());
            }

        } catch (FileNotFoundException e) {
            throw new LoxxerException(e.getMessage(), e);
        } catch (LoxxerException e) {
            throw e;
        }
    }
}
