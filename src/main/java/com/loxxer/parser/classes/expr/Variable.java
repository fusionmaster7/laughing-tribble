package com.loxxer.parser.classes.expr;

import com.loxxer.lexical.LexicalToken;
import com.loxxer.visitor.IVisitor;

public class Variable extends Expr {
    public LexicalToken token;

    public Variable(LexicalToken token) {
        this.token = token;
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visitVariableExpr(this);
    }

}
