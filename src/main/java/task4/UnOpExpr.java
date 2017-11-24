package task4;

import java.util.Map;

public class UnOpExpr extends ExprNode
{
    private ExprNode right;
    private Token op;
    public UnOpExpr(Token op, ExprNode right)
    {
        this.op = op;
        this.right = right;
    }
    @Override
    public double eval(Map<String, Double> vars)
    {
        double rightValue = right.eval(vars);
        try
        {
            return op.type.unaryOperator(rightValue);
        }
        catch (UnsupportedOperationException e)
        {
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString()
    {
        return op.text + "("+right.toString()+")";
    }
}
