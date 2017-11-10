package task4;

import java.util.Map;

public class NumbExpr extends ExprNode
{
    private Token number;
    public NumbExpr(Token number)
    {
        this.number = number;
    }
    @Override
    public double eval(Map<String, Double> vars)
    {
        return Double.parseDouble(number.text);
    }

    @Override
    public String toString()
    {
        return number.text;
    }
}
