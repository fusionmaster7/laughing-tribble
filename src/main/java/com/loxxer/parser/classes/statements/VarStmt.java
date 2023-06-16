package com.loxxer.parser.classes.statements;

import com.loxxer.lexical.LexicalToken;
import com.loxxer.parser.classes.expr.Expr;
import com.loxxer.visitor.IStmtVisitor;

public class VarStmt extends Stmt {
    public LexicalToken token;
    public Expr expr;

    public VarStmt(LexicalToken token) {
        this.token = token;
        this.expr = null;
    }

    public VarStmt(LexicalToken token, Expr expr) {
        this.token = token;
        this.expr = expr;
    }

    @Override
    public <T> T accept(IStmtVisitor<T> visitor) {
        return visitor.visitVarStmt(this);
    }
}
