package com.loxxer.visitor;

import com.loxxer.parser.classes.statements.BlockStmt;
import com.loxxer.parser.classes.statements.ExprStmt;
import com.loxxer.parser.classes.statements.IfStmt;
import com.loxxer.parser.classes.statements.PrintStmt;
import com.loxxer.parser.classes.statements.VarStmt;
import com.loxxer.parser.classes.statements.WhileStmt;
import com.loxxer.parser.classes.statements.ForStmt;

public interface IStmtVisitor<T> {
    public T visitExprStmt(ExprStmt statement);

    public T visitPrintStmt(PrintStmt statement);

    public T visitVarStmt(VarStmt statement);

    public T visitBlockStmt(BlockStmt statement);

    public T visitIfStmt(IfStmt statement);

    public T visitWhileStmt(WhileStmt statement);

    public T visitForStmt(ForStmt statement);
}
