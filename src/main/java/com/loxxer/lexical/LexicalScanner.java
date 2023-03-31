package com.loxxer.lexical;

import java.util.ArrayList;
import java.util.HashMap;
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

    private HashMap<String, LexicalTokenType> keywords;

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
        this.keywords = new HashMap<String, LexicalTokenType>();
        this.addKeywords();
    }

    // To add keywords to the hashmap
    private void addKeywords() {
        keywords.put("and", LexicalTokenType.AND);
        keywords.put("class", LexicalTokenType.CLASS);
        keywords.put("else", LexicalTokenType.ELSE);
        keywords.put("false", LexicalTokenType.FALSE);
        keywords.put("for", LexicalTokenType.FOR);
        keywords.put("fun", LexicalTokenType.FUN);
        keywords.put("if", LexicalTokenType.IF);
        keywords.put("nil", LexicalTokenType.NIL);
        keywords.put("or", LexicalTokenType.OR);
        keywords.put("print", LexicalTokenType.PRINT);
        keywords.put("return", LexicalTokenType.RETURN);
        keywords.put("super", LexicalTokenType.SUPER);
        keywords.put("this", LexicalTokenType.THIS);
        keywords.put("true", LexicalTokenType.TRUE);
        keywords.put("var", LexicalTokenType.VAR);
        keywords.put("while", LexicalTokenType.WHILE);
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
        return ch;
    }

    private String getLexemme() {
        return this.source.substring(this.start, this.current);
    }

    // To match for 2 character operators. <=, >= , != , ==
    private boolean match(char ch) {
        if (this.isAtEnd()) {
            return false;
        }

        if (this.source.charAt(this.current) != ch) {
            return false;
        }

        this.current++;

        return true;
    }

    // To peek at the current character without consuming it. Look ahead pointer
    private char peek() {
        if (this.isAtEnd()) {
            return '\0';
        } else {
            return this.source.charAt(current);
        }
    }

    // To check if given char is a digit or not
    private boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    private boolean isAlpha(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_';
    }

    private char peekNext() {
        if (this.current + 1 >= this.source.length()) {
            return '\0';
        } else {
            return this.source.charAt(current + 1);
        }
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
            case '/':
                // Check for comments
                if (match('/')) {
                    while (peek() != '\n' && !this.isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(LexicalTokenType.SLASH);
                }
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
            case '>':
                if (match('=')) {
                    addToken(LexicalTokenType.GREATER_EQUAL);
                } else {
                    addToken(LexicalTokenType.GREATER);
                }
                break;
            case '<':
                if (match('=')) {
                    addToken(LexicalTokenType.LESS_EQUAL);
                } else {
                    addToken(LexicalTokenType.LESS);
                }
                break;
            case '=':
                if (match('=')) {
                    addToken(LexicalTokenType.DOUBLE_EQUAL);
                } else {
                    addToken(LexicalTokenType.EQUAL);
                }
                break;
            case '!':
                if (match('=')) {
                    addToken(LexicalTokenType.BANG_EQUAL);
                } else {
                    addToken(LexicalTokenType.BANG);
                }
                break;
            // For string literals
            case '"':
                while (peek() != '"' && !this.isAtEnd()) {
                    char next = advance();
                    if (next == '\n') {
                        this.lineNumber++;
                    }
                }
                if (this.isAtEnd()) {
                    this.errorHandler.reportError(
                            new LexicalError(this.getLexemme(), "Unterminated string literal", this.lineNumber));
                } else {
                    advance();
                    String lexemme = source.substring(start + 1, current - 1);
                    addToken(LexicalTokenType.STRING, lexemme);
                }
                break;
            case ' ':
                break;
            case '\r':
                break;
            case '\t':
                break;
            case '\n':
                this.lineNumber++;
                break;
            default:
                if (isDigit(ch)) {
                    while (isDigit(peek())) {
                        advance();
                    }

                    if (peek() == '.' && isDigit(peekNext())) {
                        advance();
                    }

                    while (isDigit(peek())) {
                        advance();
                    }

                    String lexemme = this.source.substring(this.start, this.current);
                    this.addToken(LexicalTokenType.NUMBER, Double.parseDouble(lexemme));
                } else if (isAlpha(ch)) {
                    while (isAlpha(peek())) {
                        advance();
                    }

                    String lexemme = this.source.substring(this.start, this.current);
                    LexicalTokenType tokenType = this.keywords.get(lexemme);
                    if (tokenType == null) {
                        this.addToken(LexicalTokenType.IDENTIFIER);
                    } else {
                        this.addToken(tokenType);
                    }

                } else {
                    this.errorHandler.reportError(
                            new LexicalError(this.getLexemme(), "Unidentified lexical token", this.lineNumber));

                }
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
