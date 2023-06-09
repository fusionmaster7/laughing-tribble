package com.loxxer.parser.classes.visitor;

import com.loxxer.parser.classes.expr.*;

public interface IVisitor<T> {

	public T visitBinaryExpr(Binary expr);

	public T visitUnaryExpr(Unary expr);

	public T visitGroupingExpr(Grouping expr);

	public T visitLiteralExpr(Literal expr);
}