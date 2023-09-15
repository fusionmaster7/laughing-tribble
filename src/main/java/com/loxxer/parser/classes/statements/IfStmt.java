package com.loxxer.parser.classes.statements;

import com.loxxer.parser.classes.expr.Expr;
import com.loxxer.visitor.IStmtVisitor;

public class IfStmt extends Stmt {
    public Expr condition;
    public Stmt statement;
    public Stmt elseStatement;

    public IfStmt() {
        // This is done so the interpreter can handle the cases where there is IF
        // without ELSE
        this.elseStatement = null;
    }

    @Override
    public <T> T accept(IStmtVisitor<T> visitor) {
        return visitor.visitIfStmt(this);
    }
}
