package task4;

import java.util.Map;

public class FuncExpNode extends ExprNode{
    final Function function;
    final Double[] values;

    public FuncExpNode(Function function, Double[] values) {
        this.function = function;
        this.values = values;
    }


    @Override
    public double eval(Map<String, Double> vars) {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
