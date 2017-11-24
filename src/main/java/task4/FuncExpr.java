package task4;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class FuncExpr extends ExprNode
{
    //Function<Variable, > cons;
    FuncExpr()
    {

    }

    @Override
    public double eval(Map<String, Double> vars)
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return null;
    }
}
