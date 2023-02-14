package com.loxxer.tokens;

/**
 * Class to represent a token in the Lox programming language
 */
public class LoxxerToken {
    private String lexenne;
    private LoxxerTokenType tokenType;
    private int lineNumber;

    private LoxxerToken(String lexenne, LoxxerTokenType tokenType, int lineNumber) {
        this.lexenne = lexenne;
        this.tokenType = tokenType;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "Token of type " + this.tokenType + ". Found on line: " + this.lineNumber;
    }

    /**
     * Static method to create a new token
     *
     * @param lexenne    The token string
     * @param tokenType  The token type
     * @param lineNumber The line where that particular token was found. Useful for
     *                   error reporting
     * @return A LoxxerTokenType Object denoting a token
     */
    public static LoxxerToken createToken(String lexenne, LoxxerTokenType tokenType, int lineNumber) {
        return new LoxxerToken(lexenne, tokenType, lineNumber);
    }
}
