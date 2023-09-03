package com.loxxer.visitor;

import com.loxxer.environment.Environment;
import com.loxxer.error.ErrorHandler;
import com.loxxer.lexical.LexicalToken;
import com.loxxer.parser.RuntimeError;
import com.loxxer.parser.classes.expr.Assign;
import com.loxxer.parser.classes.expr.Binary;
import com.loxxer.parser.classes.expr.Grouping;
import com.loxxer.parser.classes.expr.Literal;
import com.loxxer.parser.classes.expr.Unary;
import com.loxxer.parser.classes.expr.Variable;

/*
 * Class that traverses the syntax tree and evaluates the expression
 */
public class ExprVisitor implements IVisitor<Object> {
    private ErrorHandler errorHandler;
    private Environment environment;

    // Check if operand is of the class Double or not
    private boolean checkNumberOperand(LexicalToken operator, Object operand) throws RuntimeError {
        if (operand instanceof Double) {
            return true;
        }

        throw error(operator, "Operand must be of the type Double");
    }

    private boolean checkNumberOperands(LexicalToken operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return true;
        }

        throw error(operator, "Operands must be both of the type Double");
    }

    private RuntimeError error(LexicalToken token, String message) {
        RuntimeError error = new RuntimeError(token, message);
        this.errorHandler.reportError(error);
        return error;
    }

    private boolean isTruthy(Object object) {
        if (object == null) {
            return false;
        } else if (object instanceof Boolean) {
            return (boolean) object;
        }

        return true;
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }

        if (left == null) {
            return false;
        }

        return left.equals(right);
    }

    public ExprVisitor(ErrorHandler errorHandler, Environment environment) {
        this.errorHandler = errorHandler;
        this.environment = environment;
    }

    @Override
    public Object visitBinaryExpr(Binary expr) throws RuntimeError {
        Object left = expr.left.accept(this);
        Object right = expr.right.accept(this);

        switch (expr.op.getTokenType()) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;

                } else if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }

                throw error(expr.op, "Operands must be both of the type double or string");

            case MINUS:
                checkNumberOperands(expr.op, left, right);
                return (double) left - (double) right;
            case STAR:
                checkNumberOperands(expr.op, left, right);
                return (double) left * (double) right;
            case SLASH:
                checkNumberOperands(expr.op, left, right);
                return (double) left / (double) right;

            case GREATER:
                checkNumberOperands(expr.op, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.op, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.op, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.op, left, right);
                return (double) left <= (double) right;

            case EQUAL:
                return isEqual(left, right);
            case BANG_EQUAL:
                return !isEqual(left, right);
            default:
                break;
        }

        return null;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) throws RuntimeError {
        Object right = expr.right.accept(this);

        switch (expr.op.getTokenType()) {
            // Unary minus operator
            case MINUS:
                checkNumberOperand(expr.op, right);
                return -(double) right;
            // Logical not operator
            case BANG:
                return !isTruthy(right);
            default:
                return null;
        }
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) throws RuntimeError {
        return expr.expr.accept(this);
    }

    @Override
    public Object visitLiteralExpr(Literal expr) throws RuntimeError {
        return expr.value;
    }

    @Override
    public Object visitVariableExpr(Variable expr) throws RuntimeError {
        String variable = expr.token.getLexemme();

        return this.environment.get(variable);
    }

    @Override
    public Object visitAssignExpr(Assign expr) {

        Object value = expr.value.accept(this);
        this.environment.set(expr.token.getLexemme(), value);

        return value;
    }
}
