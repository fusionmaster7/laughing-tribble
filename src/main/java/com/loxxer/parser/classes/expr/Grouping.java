package com.loxxer.parser.classes.expr;

import com.loxxer.visitor.IVisitor;

public class Grouping extends Expr {
	public final Expr expr;

	public Grouping(Expr expr) {
		this.expr = expr;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visitGroupingExpr(this);
	}
}