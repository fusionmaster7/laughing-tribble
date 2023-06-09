package com.loxxer.parser.classes;

import com.loxxer.parser.classes.expr.Binary;
import com.loxxer.parser.classes.expr.Grouping;
import com.loxxer.parser.classes.expr.Literal;
import com.loxxer.parser.classes.expr.Unary;

public class ASTVisitor implements IVisitor<String> {
    @Override
    public String visitBinaryExpr(Binary expr) {
        return brackets(expr.op.getLexemme() + " " + expr.left.accept(this) + " "
                + expr.right.accept(this));
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return brackets(expr.op.getLexemme() + " " + expr.right.accept(this));
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return "(" + expr.expr.accept(this) + ")";
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        return expr.value.toString();
    }

    public String brackets(String s) {
        return "[" + s + "]";
    }
}
