package com.loxxer.parser.classes.statements;

import com.loxxer.parser.classes.statements.Stmt;
import com.loxxer.parser.classes.expr.Expr;
import com.loxxer.visitor.IStmtVisitor;

public class WhileStmt extends Stmt {
    public Expr condition;
    public Stmt body;

    public WhileStmt() {
	this.condition = null;
	this.body = null;
    }

    @Override
    public<T> T accept(IStmtVisitor<T> visitor) {
	return visitor.visitWhileStmt(this);
    }
}
