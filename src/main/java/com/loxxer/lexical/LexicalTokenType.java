package com.loxxer.lexical;

public enum LexicalTokenType {
    // Single character tokens
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    PLUS, MINUS, STAR, SLASH,
    COMMA, DOT, SEMICOLON,

    // Single OR Double Character Tokens
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    EQUAL, DOUBLE_EQUAL,
    BANG, BANG_EQUAL, // BANG IS !

    // End of file token
    EOF
}
