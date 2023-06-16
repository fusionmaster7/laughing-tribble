package com.loxxer.parser;

import java.util.ArrayList;
import java.util.List;

import com.loxxer.error.ErrorHandler;
import com.loxxer.lexical.LexicalToken;
import com.loxxer.lexical.LexicalTokenType;
import com.loxxer.parser.classes.expr.Expr;
import com.loxxer.parser.classes.expr.Grouping;
import com.loxxer.parser.classes.expr.Binary;
import com.loxxer.parser.classes.expr.Literal;
import com.loxxer.parser.classes.expr.Unary;
import com.loxxer.parser.classes.expr.Variable;
import com.loxxer.parser.classes.statements.ExprStmt;
import com.loxxer.parser.classes.statements.PrintStmt;
import com.loxxer.parser.classes.statements.Stmt;
import com.loxxer.parser.classes.statements.VarStmt;

// The parser implements the grammar as specified in the lox.grammar file
public class Parser {
    private List<LexicalToken> tokens;
    private int current = 0;
    private ErrorHandler errorHandler;

    public Parser(List<LexicalToken> tokens, ErrorHandler errorHandler) {
        this.tokens = tokens;
        this.errorHandler = errorHandler;
    }

    private LexicalToken peek() {
        return this.tokens.get(current);
    }

    private LexicalToken previous() {
        return this.tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return this.peek().getTokenType() == LexicalTokenType.EOF;
    }

    private ParsingError error(LexicalToken token, String message) {
        ParsingError parsingError = new ParsingError(token, message);
        this.errorHandler.reportError(parsingError);

        return parsingError;
    }

    /**
     * Used to synchronise back to valid state after entering panic mode.
     */
    private void synchronise() {
        advance();

        while (!isAtEnd()) {
            if (previous().getTokenType() == LexicalTokenType.SEMICOLON) {
                return;
            }

            switch (peek().getTokenType()) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
                default:
            }

            advance();
        }

    }

    /**
     * Consumes a token only if it matches the given type. Otherwise interpreter
     * enters panic mode.
     *
     * @param tokenType Type of token to be consumed
     * @return LexicalToken of given type
     */
    private LexicalToken consume(LexicalTokenType tokenType, String message) {
        if (peek().getTokenType() == tokenType) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean check(LexicalToken token, LexicalTokenType tokenType) {
        if (isAtEnd()) {
            return false;
        }
        return token.getTokenType() == tokenType;
    }

    private LexicalToken advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean match(LexicalTokenType... tokenTypes) {
        for (LexicalTokenType tokenType : tokenTypes) {
            if (check(peek(), tokenType)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Expr primary() {

        if (match(LexicalTokenType.FALSE)) {
            return new Literal(false);
        }

        if (match(LexicalTokenType.TRUE)) {
            return new Literal(true);
        }

        if (match(LexicalTokenType.NIL)) {
            return new Literal(null);
        }

        if (match(LexicalTokenType.NUMBER, LexicalTokenType.STRING)) {
            return new Literal(previous().getLiteral());
        }

        if (match(LexicalTokenType.LEFT_PAREN)) {
            Expr expr = expr();
            consume(LexicalTokenType.RIGHT_PAREN, ") expected");
            return new Grouping(expr);
        }

        if (match(LexicalTokenType.IDENTIFIER)) {
            return new Variable(previous());
        }

        throw error(peek(), "Expect Expression");
    }

    private Expr unary() {
        if (match(LexicalTokenType.BANG, LexicalTokenType.MINUS)) {
            LexicalToken op = previous();
            Expr right = unary();
            return new Unary(op, right);
        }

        return primary();
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(LexicalTokenType.SLASH, LexicalTokenType.STAR)) {
            LexicalToken op = previous();
            Expr right = unary();
            expr = new Binary(expr, op, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(LexicalTokenType.MINUS, LexicalTokenType.PLUS)) {
            LexicalToken op = previous();
            Expr right = factor();
            expr = new Binary(expr, op, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(LexicalTokenType.GREATER, LexicalTokenType.GREATER_EQUAL, LexicalTokenType.LESS,
                LexicalTokenType.LESS_EQUAL)) {
            LexicalToken op = previous();
            Expr right = term();
            expr = new Binary(expr, op, right);
        }
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(LexicalTokenType.BANG_EQUAL, LexicalTokenType.EQUAL)) {
            LexicalToken op = previous();
            Expr right = comparison();
            expr = new Binary(expr, op, right);
        }
        return expr;
    }

    private Expr expr() {
        Expr finalExpr = equality();
        return finalExpr;
    }

    private ExprStmt exprStmt() {
        Expr expr = expr();
        if (!match(LexicalTokenType.SEMICOLON)) {
            throw error(peek(), "Semicolon missing");
        }

        return new ExprStmt(expr);
    }

    private PrintStmt printStmt() {
        Expr expr = expr();
        if (!match(LexicalTokenType.SEMICOLON)) {
            throw error(peek(), "Semicolon missing");
        }

        return new PrintStmt(expr);
    }

    private Stmt statement() {
        if (match(LexicalTokenType.PRINT)) {
            return printStmt();
        } else {
            return exprStmt();
        }
    }

    private Stmt varDeclaration() {
        if (match(LexicalTokenType.IDENTIFIER)) {
            LexicalToken token = previous();
            if (match(LexicalTokenType.EQUAL)) {
                Expr expr = expr();

                if (!match(LexicalTokenType.SEMICOLON)) {
                    throw error(peek(), "Semicolon Missing");

                }

                return new VarStmt(token, expr);
            } else if (match(LexicalTokenType.SEMICOLON)) {
                return new VarStmt(token);
            } else {
                throw error(peek(), "Semicolon Missing");
            }
        } else {
            throw error(peek(), "Identifier missing. Identifier name must be specified");
        }

    }

    private Stmt declaration() {
        if (match(LexicalTokenType.VAR)) {
            return varDeclaration();
        } else {
            return statement();
        }
    }

    public List<Stmt> parse() throws ParsingError {
        try {
            List<Stmt> declarations = new ArrayList<Stmt>();
            while (!isAtEnd()) {
                Stmt declaration = declaration();
                declarations.add(declaration);
            }

            return declarations;
        } catch (ParsingError e) {
            throw e;
        }
    }
}
