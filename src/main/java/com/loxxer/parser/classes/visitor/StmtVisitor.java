package com.loxxer.parser.classes.visitor;

import com.loxxer.error.ErrorHandler;
import com.loxxer.parser.classes.statements.ExprStmt;
import com.loxxer.parser.classes.statements.PrintStmt;

public class StmtVisitor implements IStmtVisitor<Object> {
    private ErrorHandler errorHandler;

    public StmtVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public Object visitExprStmt(ExprStmt statement) {
        ExprVisitor visitor = new ExprVisitor(errorHandler);
        return statement.expr.accept(visitor);
    }

    @Override
    public Object visitPrintStmt(PrintStmt statement) {
        ExprVisitor visitor = new ExprVisitor(errorHandler);
        Object value = statement.expr.accept(visitor);
        System.out.println(value.toString());
        return null;
    }
}
