package com.loxxer.parser.classes.statements;

import java.util.ArrayList;
import java.util.List;

import com.loxxer.visitor.IStmtVisitor;

public class BlockStmt extends Stmt {
    public List<Stmt> statements;

    public BlockStmt() {
        this.statements = new ArrayList<Stmt>();
    }

    @Override
    public <T> T accept(IStmtVisitor<T> visitor) {
        return visitor.visitBlockStmt(this);
    }

    public void addDeclaration(Stmt statement) {
        this.statements.add(statement);
    }
}
