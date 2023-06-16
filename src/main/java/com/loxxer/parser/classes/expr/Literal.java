package com.loxxer.parser.classes.expr;

import com.loxxer.visitor.IVisitor;

public class Literal extends Expr {
	public final Object value;

	public Literal(Object value) {
		this.value = value;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visitLiteralExpr(this);
	}
}