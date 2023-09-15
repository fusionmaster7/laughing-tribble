package com.loxxer.parser.classes.expr;

import com.loxxer.visitor.IVisitor;

// Base Class to represent expressions
public abstract class Expr {
	public abstract <T> T accept(IVisitor<T> visitor);
}