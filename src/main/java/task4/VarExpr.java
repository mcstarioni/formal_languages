package task4;

import java.util.Map;

public class VarExpr extends ExprNode
{

    public final Token var;
    public VarExpr(Token var)
    {
        this.var = var;
    }
    @Override
    public double eval(Map<String, Double> vars) throws ParseException
    {
        Double d;
        if ((d = vars.get(var.text)) != null)
            return d.doubleValue();
        else
            throw new ParseException("Переменная не найдена!", 0);
    }
    @Override
    public String toString()
    {
        return var.text;
    }
}
