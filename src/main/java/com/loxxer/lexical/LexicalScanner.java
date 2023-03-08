package com.loxxer.lexical;

import java.util.ArrayList;
import java.util.List;

import com.loxxer.error.ErrorHandler;

/*
 * Lexical Scanner Class
 * Takes a source and returns a list of tokens
 */
public class LexicalScanner {
    private String source;
    private List<LexicalToken> tokens;

    private int start;
    private int current;
    private int lineNumber;

    private ErrorHandler errorHandler;

    /**
     * Lexical Scanner Class Constructor
     *
     * @param source - Source code as a string
     */
    public LexicalScanner(String source, ErrorHandler errorHandler) {
        this.source = source;
        this.tokens = new ArrayList<LexicalToken>();

        this.start = 0;
        this.current = 0;
        this.lineNumber = 1;

        this.errorHandler = errorHandler;
    }

    private boolean isAtEnd() {
        return this.current >= this.source.length();
    }

    private void addToken(LexicalTokenType tokenType, Object literal) {
        String lexemme = this.source.substring(this.start, this.current);
        this.tokens.add(new LexicalToken(lexemme, lineNumber, tokenType, literal));
    }

    private void addToken(LexicalTokenType tokenType) {
        this.addToken(tokenType, null);
    }

    private char advance() {
        char ch = this.source.charAt(this.current);
        this.current++;
        if (ch == '\n') {
            this.lineNumber++;
        }
        return ch;
    }

    public void scanToken() {
        char ch = this.advance();
        switch (ch) {
            case '(':
                addToken(LexicalTokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(LexicalTokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(LexicalTokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(LexicalTokenType.RIGHT_BRACE);
                break;
            case '+':
                addToken(LexicalTokenType.PLUS);
                break;
            case '-':
                addToken(LexicalTokenType.MINUS);
                break;
            case '*':
                addToken(LexicalTokenType.STAR);
                break;
            case ';':
                addToken(LexicalTokenType.SEMICOLON);
                break;
            case ',':
                addToken(LexicalTokenType.COMMA);
                break;
            case '.':
                addToken(LexicalTokenType.DOT);
                break;
            case '\n':
                break;
            default:
                this.errorHandler.reportError(
                        new LexicalError(this.source.substring(this.start, this.current), this.lineNumber));
                break;
        }
    }

    public List<LexicalToken> scan(String source) {

        while (!this.isAtEnd()) {
            this.start = this.current;
            scanToken();
        }

        tokens.add(new LexicalToken("", lineNumber, LexicalTokenType.EOF, null));
        return this.tokens;
    }

}
