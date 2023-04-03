package com.loxxer.parser;

import com.loxxer.lexical.LexicalToken;
import com.loxxer.lexical.LexicalTokenType;
import com.loxxer.parser.classes.ASTVisitor;
import com.loxxer.parser.classes.expr.Binary;
import com.loxxer.parser.classes.expr.Grouping;
import com.loxxer.parser.classes.expr.Literal;
import com.loxxer.parser.classes.expr.Unary;

public class Parser {
    public void printAST() {
        LexicalToken plusToken = new LexicalToken("+", 1, LexicalTokenType.PLUS, null);
        LexicalToken minusToken = new LexicalToken("-", 1, LexicalTokenType.MINUS, null);

        Binary expr = new Binary(new Unary(minusToken, new Literal(24)), plusToken, new Grouping(new Literal(23)));

        ASTVisitor visitor = new ASTVisitor();
        System.out.println(expr.accept(visitor));
    }
}
