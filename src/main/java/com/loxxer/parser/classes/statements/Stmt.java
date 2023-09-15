package com.loxxer.parser.classes.statements;

import com.loxxer.visitor.IStmtVisitor;

// Base class to represent statements
public abstract class Stmt {
    public abstract <T> T accept(IStmtVisitor<T> visitor);
}
