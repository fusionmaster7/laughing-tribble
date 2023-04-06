package com.loxxer.parser;

import com.loxxer.error.LoxxerError;
import com.loxxer.lexical.LexicalToken;

public class ParsingError extends LoxxerError {
    private LexicalToken token;
    private String message;

    public ParsingError(LexicalToken token, String message) {
        this.token = token;
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return "[Parsing Error] on token '" + this.token.getLexemme() + "' at line " + token.getLineNumber() + ": "
                + this.message;
    }
}
