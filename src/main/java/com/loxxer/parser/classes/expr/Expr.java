package com.loxxer.parser.classes.expr;

import com.loxxer.parser.classes.IVisitor;

public abstract class Expr {
	public abstract <T> T accept(IVisitor<T> visitor);
}