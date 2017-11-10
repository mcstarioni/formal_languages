package task4;

import java.util.Map;

public class VarExpr extends ExprNode
{

    private Token var;
    public VarExpr(Token var)
    {
        this.var = var;
    }
    @Override
    public double eval(Map<String, Double> vars)
    {
        return vars.get(var.text);
    }
    @Override
    public String toString()
    {
        return null;
    }
}
