package com.loxxer.parser.classes.statements;

import com.loxxer.parser.classes.expr.Expr;
import com.loxxer.visitor.IStmtVisitor;

public class PrintStmt extends Stmt {
    public Expr expr;

    public PrintStmt(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(IStmtVisitor<T> visitor) {
        return visitor.visitPrintStmt(this);
    }
}
