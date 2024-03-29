package com.loxxer.visitor;

import com.loxxer.environment.Environment;
import com.loxxer.error.ErrorHandler;
import com.loxxer.parser.classes.statements.BlockStmt;
import com.loxxer.parser.classes.statements.ExprStmt;
import com.loxxer.parser.classes.statements.IfStmt;
import com.loxxer.parser.classes.statements.PrintStmt;
import com.loxxer.parser.classes.statements.Stmt;
import com.loxxer.parser.classes.statements.VarStmt;
import com.loxxer.parser.classes.statements.WhileStmt;
import com.loxxer.parser.classes.statements.ForStmt;

public class StmtVisitor implements IStmtVisitor<Object> {
    private ErrorHandler errorHandler;
    private Environment environment;

    private Boolean isTruthy(Object object) {
        if (object == null) {
            return false;
        } else if (object instanceof Boolean) {
            return (boolean) object;
        }

        return true;
    }

    public StmtVisitor(Environment environment, ErrorHandler errorHandler) {
        this.environment = environment;
        this.errorHandler = errorHandler;
    }

    // To change the environment in case of block scope
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Object visitExprStmt(ExprStmt statement) {
        ExprVisitor visitor = new ExprVisitor(errorHandler, environment);
        return statement.expr.accept(visitor);
    }

    @Override
    public Object visitPrintStmt(PrintStmt statement) {
        ExprVisitor visitor = new ExprVisitor(errorHandler, environment);
        Object value = statement.expr.accept(visitor);
        System.out.println(value.toString());
        return null;
    }

    @Override
    public Object visitVarStmt(VarStmt statement) {
        ExprVisitor visitor = new ExprVisitor(errorHandler, environment);
        Object value = statement.expr.accept(visitor);

        environment.define(statement.token, value);

        return value;
    }

    @Override
    public Object visitBlockStmt(BlockStmt statement) {
        Environment oldEnvironment = this.environment;
        Environment newEnvironment = new Environment(oldEnvironment);
        setEnvironment(newEnvironment);

        for (Stmt stmt : statement.statements) {
            stmt.accept(this);
        }

        setEnvironment(oldEnvironment);
        return null;
    }

    @Override
    public Object visitIfStmt(IfStmt ifStmt) {
        ExprVisitor visitor = new ExprVisitor(errorHandler, environment);

        // Evaluate the condition inside the if statement
        Object value = ifStmt.condition.accept(visitor);

        if (isTruthy(value)) {
            StmtVisitor stmtVisitor = new StmtVisitor(environment, errorHandler);
            return ifStmt.statement.accept(stmtVisitor);
        } else if (ifStmt.elseStatement != null) {
            StmtVisitor stmtVisitor = new StmtVisitor(environment, errorHandler);
            return ifStmt.elseStatement.accept(stmtVisitor);
        }

        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt whileStmt) {
	ExprVisitor visitor = new ExprVisitor(errorHandler, environment);
	StmtVisitor stmtVisitor = new StmtVisitor(environment, errorHandler);
	while(isTruthy(whileStmt.condition.accept(visitor))) {
	    // Evaluate the loop statement. If it returns a non-null value, return the 
	    // value
	    whileStmt.body.accept(stmtVisitor);	    
	}
	return null;
    }

    @Override
    public Object visitForStmt(ForStmt forStmt) {
	ExprVisitor visitor = new ExprVisitor(errorHandler, environment);
	StmtVisitor stmtVisitor = new StmtVisitor(environment, errorHandler);
	forStmt.init.accept(stmtVisitor);

	while (isTruthy(forStmt.condition.accept(visitor))) {
	    forStmt.body.accept(stmtVisitor);
	    forStmt.update.accept(visitor);
	}
	return null;
    }
}
