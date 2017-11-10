package task4;

import java.util.Map;

public abstract class ExprNode
{
    public abstract double eval(Map<String, Double> vars);
    public abstract String toString();
}
