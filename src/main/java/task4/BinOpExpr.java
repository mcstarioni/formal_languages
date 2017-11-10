package task4;

import java.util.Map;

public class BinOpExpr extends ExprNode
{
    private ExprNode left;
    private ExprNode right;
    private Token op;
    public BinOpExpr(ExprNode left, Token op, ExprNode right)
    {
        this.left = left;
        this.op = op;
        this.right = right;
    }
    @Override
    public double eval(Map<String, Double> vars)
    {
        double leftValue = left.eval(vars);
        double rightValue = right.eval(vars);
        switch (op.type) {
            case ADD: return leftValue + rightValue;
            case SUB: return leftValue - rightValue;
            case MUL: return leftValue * rightValue;
            case DIV: return leftValue / rightValue;
        }
        throw new IllegalStateException();
    }
    @Override
    public String toString()
    {
        return "(" + left.toString() + op.text + right.toString() + ")";
    }
}
