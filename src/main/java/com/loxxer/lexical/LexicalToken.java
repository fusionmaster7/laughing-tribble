package com.loxxer.lexical;

/**
 * Class to represent a lexical token in the Lox programming language
 */
public class LexicalToken {
    // The token value
    private String lexemme;
    // Line where it was scanned
    private int lineNumber;
    // Token Type
    private LexicalTokenType tokenType;
    private Object literal;

    public LexicalToken(String lexemme, int lineNumber, LexicalTokenType tokenType, Object literal) {
        this.lexemme = lexemme;
        this.lineNumber = lineNumber;
        this.tokenType = tokenType;
        this.literal = literal;
    }

    @Override
    public String toString() {
        String value = "";
        if (this.tokenType == LexicalTokenType.STRING) {
            value = this.literal.toString();
        } else {
            value = this.lexemme;
        }

        return "Token type " + this.tokenType + " having value " + value + " on line number "
                + this.lineNumber;

    }
}
