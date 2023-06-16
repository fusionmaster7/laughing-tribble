package com.loxxer.parser.classes.expr;

import com.loxxer.lexical.LexicalToken;
import com.loxxer.visitor.IVisitor;

public class Unary extends Expr {
	public final LexicalToken op;
	public final Expr right;

	public Unary(LexicalToken op, Expr right) {
		this.op = op;
		this.right = right;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visitUnaryExpr(this);
	}
}