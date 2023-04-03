package com.loxxer.parser.classes;

import com.loxxer.parser.classes.expr.*;

public interface IVisitor<T> {

	public T visitBinaryExpr(Binary expr);

	public T visitUnaryExpr(Unary expr);

	public T visitGroupingExpr(Grouping expr);

	public T visitLiteralExpr(Literal expr);
}