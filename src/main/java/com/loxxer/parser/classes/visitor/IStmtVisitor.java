package com.loxxer.parser.classes.visitor;

import com.loxxer.parser.classes.statements.ExprStmt;
import com.loxxer.parser.classes.statements.PrintStmt;

public interface IStmtVisitor<T> {
    public T visitExprStmt(ExprStmt statement);

    public T visitPrintStmt(PrintStmt statement);
}
