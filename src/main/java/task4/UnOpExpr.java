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
        switch (op.type)
        {
            case SUB:
                return -rightValue;
            case EXCLAM:
                return factorial((long) rightValue);
        }
        throw new IllegalStateException();
    }
    private static long factorial(long n) {
        long result = 1;
        for (long i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    @Override
    public String toString()
    {
        return op.text + right.toString();
    }
}
