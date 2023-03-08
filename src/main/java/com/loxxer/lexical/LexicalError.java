package com.loxxer.lexical;

import com.loxxer.error.LoxxerError;

public class LexicalError implements LoxxerError {
    private String lexemme;
    private int lineNumber;

    public LexicalError(String lexemme, int lineNumber) {
        this.lexemme = lexemme;
        this.lineNumber = lineNumber;
    }

    @Override
    public String getErrorMessage() {
        return "[Lexical Error]: Unidentified Lexemme " + this.lexemme + " on line number " + this.lineNumber;
    }
}
