package com.loxxer.parser.classes.statements;

import com.loxxer.parser.classes.expr.Expr;
import com.loxxer.visitor.IStmtVisitor;

public class ExprStmt extends Stmt {
    public Expr expr;

    public ExprStmt(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(IStmtVisitor<T> visitor) {
        return visitor.visitExprStmt(this);
    }
}
