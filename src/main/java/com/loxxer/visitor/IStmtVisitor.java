package com.loxxer.visitor;

import com.loxxer.parser.classes.statements.ExprStmt;
import com.loxxer.parser.classes.statements.PrintStmt;
import com.loxxer.parser.classes.statements.VarStmt;

public interface IStmtVisitor<T> {
    public T visitExprStmt(ExprStmt statement);

    public T visitPrintStmt(PrintStmt statement);

    public T visitVarStmt(VarStmt statement);
}
