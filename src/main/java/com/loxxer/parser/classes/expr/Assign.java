package com.loxxer.parser.classes.expr;

import com.loxxer.lexical.LexicalToken;
import com.loxxer.visitor.IVisitor;

public class Assign extends Expr {
    public LexicalToken token;
    public Expr value;

    public Assign(LexicalToken token, Expr value) {
        this.token = token;
        this.value = value;
    }

    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visitAssignExpr(this);
    }
}