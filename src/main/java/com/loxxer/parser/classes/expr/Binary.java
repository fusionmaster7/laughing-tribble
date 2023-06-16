package com.loxxer.parser.classes.expr;

import com.loxxer.lexical.LexicalToken;
import com.loxxer.visitor.IVisitor;

public class Binary extends Expr {
	public final Expr left;
	public final LexicalToken op;
	public final Expr right;

	public Binary(Expr left, LexicalToken op, Expr right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visitBinaryExpr(this);
	}
}