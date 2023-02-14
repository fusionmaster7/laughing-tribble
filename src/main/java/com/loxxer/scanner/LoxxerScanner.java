package com.loxxer.scanner;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.loxxer.Loxxer;
import com.loxxer.errors.LoxxerException;
import com.loxxer.tokens.LoxxerToken;
import com.loxxer.tokens.LoxxerTokenType;

/**
 * Class to perform Lexical Analysis on the source code
 */
public class LoxxerScanner {
    /**
     * Private method to add token to list
     *
     * @param lexenne    - Token String
     * @param tokenType  - Type of token
     * @param lineNumber - Line number of token
     * @param tokenList  - List of token parsed
     */
    private void addTokenToList(String lexenne, LoxxerTokenType tokenType, int lineNumber,
            List<LoxxerToken> tokenList) {
        LoxxerToken token = LoxxerToken.createToken(lexenne, tokenType, lineNumber);
        tokenList.add(token);
    }

    /**
     * Method to scan the lines in the source code. Parses the line character by
     * character and returns tokens
     *
     * @param line       - The line to parsed
     * @param lineNumber - The line number of that particular
     * @return Tokens - List of tokens in that particular line
     */
    private void scanLine(String line, int lineNumber, List<LoxxerToken> tokenList) throws LoxxerException {
        for (int i = 0; i < line.length(); i++) {
            // Scanning for special characters
            switch (line.charAt(i)) {
                case '(':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.LEFT_PAREN, lineNumber,
                            tokenList);
                    break;
                case ')':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.RIGHT_PAREN, lineNumber,
                            tokenList);
                    break;
                case '=':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.EQUALS, lineNumber,
                            tokenList);
                    break;
                case '{':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.LEFT_CURLY, lineNumber,
                            tokenList);
                    break;
                case '}':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.RIGHT_CURLY, lineNumber,
                            tokenList);
                    break;
                case ',':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.COMMA, lineNumber,
                            tokenList);
                    break;
                case '.':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.DOT, lineNumber,
                            tokenList);
                    break;
                case '-':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.MINUS, lineNumber,
                            tokenList);
                    break;
                case '*':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.STAR, lineNumber,
                            tokenList);
                    break;
                case '+':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.PLUS, lineNumber,
                            tokenList);
                    break;
                case ';':
                    this.addTokenToList(Character.toString(line.charAt(i)), LoxxerTokenType.SEMICOLON, lineNumber,
                            tokenList);
                    break;

                default:
                    throw new LoxxerException(
                            "Invalid Special Character " + line.charAt(i) + " encountered on line " + lineNumber);
            }
        }

    }

    /**
     * Method to perform lexical analysis on the source code file
     *
     * @param file InputStream Class Object for the source code file
     * @return List of tokens. The tokens are of the type LoxxerTokenType
     * @throws LoxxerException
     */
    public List<LoxxerToken> scan(FileReader fileReader) throws LoxxerException {
        List<LoxxerToken> tokenList = new ArrayList<LoxxerToken>();
        try {
            Scanner fileScanner = new Scanner(fileReader);
            int lineNumber = 0;
            while (fileScanner.hasNextLine()) {
                lineNumber++;
                scanLine(fileScanner.nextLine(), lineNumber, tokenList);
                // System.out.println(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (LoxxerException e) {
            throw e;
        }
        return tokenList;
    }

}
