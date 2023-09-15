package com.loxxer.visitor;

import com.loxxer.parser.classes.expr.*;

public interface IVisitor<T> {

	public T visitBinaryExpr(Binary expr);

	public T visitUnaryExpr(Unary expr);

	public T visitGroupingExpr(Grouping expr);

	public T visitLiteralExpr(Literal expr);

	public T visitVariableExpr(Variable expr);

	public T visitAssignExpr(Assign expr);
}