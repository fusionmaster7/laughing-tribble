package com.loxxer.parser.classes.statements;

import com.loxxer.parser.classes.statements.Stmt;
import com.loxxer.parser.classes.expr.Expr;

import com.loxxer.visitor.IStmtVisitor;

public class ForStmt extends Stmt {
    // Initialisation
    public Stmt init;
    // Condition
    public Expr condition; 
    // Update
    public Expr update;
    // Body
    public Stmt body;

    public ForStmt() {
	this.init = null;
	this.condition = null;
    	this.update = null;
	this.body = null;
    }

    @Override
    public <T> T accept (IStmtVisitor<T> visitor) {
	return visitor.visitForStmt(this);
    }
}
