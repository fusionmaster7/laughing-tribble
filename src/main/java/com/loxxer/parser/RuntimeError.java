package com.loxxer.parser;

import com.loxxer.error.LoxxerError;
import com.loxxer.error.LoxxerErrorType;
import com.loxxer.lexical.LexicalToken;

public class RuntimeError extends LoxxerError {
    private LexicalToken token;
    private String message;

    public RuntimeError(LexicalToken token, String message) {
        this.token = token;
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return "[Runtime Error] " + "on token " + this.token.getLexemme() + " at line number "
                + this.token.getLineNumber() + ": " + this.message;
    }

    @Override
    public LoxxerErrorType getErrorType() {
        return LoxxerErrorType.RUNTIME_ERROR;
    }

}
