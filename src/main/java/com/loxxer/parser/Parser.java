package com.loxxer.parser;

import java.util.ArrayList;
import java.util.List;

import com.loxxer.error.ErrorHandler;

import com.loxxer.lexical.LexicalToken;
import com.loxxer.lexical.LexicalTokenType;

import com.loxxer.parser.classes.expr.Expr;
import com.loxxer.parser.classes.expr.Grouping;
import com.loxxer.parser.classes.expr.Assign;
import com.loxxer.parser.classes.expr.Binary;
import com.loxxer.parser.classes.expr.Literal;
import com.loxxer.parser.classes.expr.Unary;
import com.loxxer.parser.classes.expr.Variable;
import com.loxxer.parser.classes.statements.BlockStmt;
import com.loxxer.parser.classes.statements.ExprStmt;
import com.loxxer.parser.classes.statements.IfStmt;
import com.loxxer.parser.classes.statements.PrintStmt;
import com.loxxer.parser.classes.statements.Stmt;
import com.loxxer.parser.classes.statements.VarStmt;
import com.loxxer.parser.classes.statements.WhileStmt;
import com.loxxer.parser.classes.statements.ForStmt;

import com.loxxer.logger.Logger;

// The parser implements the grammar as specified in the lox.grammar file
public class Parser {
    private List<LexicalToken> tokens;
    private int current = 0;
    private ErrorHandler errorHandler;
    private Logger LOGGER;

    public Parser(List<LexicalToken> tokens, ErrorHandler errorHandler) {
        this.tokens = tokens;
        this.errorHandler = errorHandler;
	this.LOGGER = new Logger(Parser.class);
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
        while (match(LexicalTokenType.BANG_EQUAL, LexicalTokenType.DOUBLE_EQUAL)) {
            LexicalToken op = previous();
            Expr right = comparison();
            expr = new Binary(expr, op, right);
        }
        return expr;
    }

    private Expr logicalAnd() {
        Expr expr = equality();
        while (match(LexicalTokenType.AND)) {
	    LexicalToken op = previous();
	    Expr right = equality();
	    expr = new Binary(expr,op,right);
        }
	return expr;
    }

    private Expr logicalOr() {
	Expr expr = logicalAnd();
	while(match(LexicalTokenType.OR)) {
	    LexicalToken op = previous();
	    Expr right = logicalAnd();
	    expr = new Binary(expr,op,right);
	}
	return expr;
    }

    private Expr assignment() {
        Expr expr = logicalOr();
        if (match(LexicalTokenType.EQUAL)) {
            LexicalToken equals = previous();
            if (expr instanceof Variable) {
                LexicalToken token = ((Variable) expr).token;
                Expr value = assignment();
                return new Assign(token, value);
            } else {
                error(peek(), "Invalid assignment after =");
            }
        } 
        return expr;
    }

    private Expr expr() {
        Expr finalExpr = assignment();
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

    private BlockStmt block() {
        BlockStmt block = new BlockStmt();

        while (!isAtEnd() && peek().getTokenType() != LexicalTokenType.RIGHT_BRACE) {
            Stmt decl = declaration();
            block.addDeclaration(decl);
        }

        advance();

        return block;
    }

    private IfStmt ifStmt() {
        IfStmt stmt = new IfStmt();

        if (match(LexicalTokenType.LEFT_PAREN)) {
            Expr condition = expr();

            if (!(match(LexicalTokenType.RIGHT_PAREN))) {
                throw error(peek(), "Expected ) after condition");
            }

            Stmt statement = statement();

            stmt.condition = condition;
            stmt.statement = statement;

            if (match(LexicalTokenType.ELSE)) {
                stmt.elseStatement = statement();
            }

        } else {
            throw error(peek(), "Expected ( after if keyword");
        }

        return stmt;
    }

    private WhileStmt whileStmt() {
	WhileStmt whileStmt = new WhileStmt();
	if (match(LexicalTokenType.LEFT_PAREN)) {
	    Expr cond = expr();
	    whileStmt.condition = cond;
	    if(match(LexicalTokenType.RIGHT_PAREN)) {
		Stmt body = statement();
		whileStmt.body = body;
	    } else {
		error(peek(), "Missing ) after the condition");
	    }
	}
	return whileStmt;
    }

    private ForStmt forStmt() {
	ForStmt forStmt = new ForStmt();
	if (match(LexicalTokenType.LEFT_PAREN)) {
	    Stmt init = null;
	    // Check for loop initialisation
	    if (match(LexicalTokenType.VAR)) {
		init = varDeclaration();
	    } else if (match(LexicalTokenType.SEMICOLON)) {
		init = null;
	    } else {
		init = exprStmt();
	    }
	    forStmt.init = init;
	    
	    // Update condition
	    if (!match(LexicalTokenType.SEMICOLON)) {
		forStmt.condition = expr();
		match(LexicalTokenType.SEMICOLON);
		forStmt.update = expr();
	    }

	    if(match(LexicalTokenType.RIGHT_PAREN)) {
		forStmt.body = statement();
	    }
	}
	return forStmt;
    }

    private Stmt statement() {
        if (match(LexicalTokenType.PRINT)) {
            return printStmt();
        } else if (match(LexicalTokenType.LEFT_BRACE)) {
            return block();
        } else if (match(LexicalTokenType.IF)) {
            return ifStmt();
        } else if (match(LexicalTokenType.WHILE)) {
	    return whileStmt();	
	} else if (match(LexicalTokenType.FOR)) {
	    return forStmt();
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
