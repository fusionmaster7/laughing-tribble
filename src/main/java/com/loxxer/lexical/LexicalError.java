package com.loxxer.lexical;

import com.loxxer.error.LoxxerError;
import com.loxxer.error.LoxxerErrorType;

public class LexicalError extends LoxxerError {
    private String lexemme;
    private String errorMessage;
    private int lineNumber;

    public LexicalError(String lexemme, String errorMessage, int lineNumber) {
        this.lexemme = lexemme;
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
    }

    @Override
    public String getErrorMessage() {
        return "[Lexical Error]: " + this.errorMessage + " on lexemme " + this.lexemme + " on line number "
                + this.lineNumber;
    }

    @Override
    public LoxxerErrorType getErrorType() {
        return LoxxerErrorType.LEXICAL_ERROR;
    }

}
