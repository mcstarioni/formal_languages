package task4;

import java.util.Map;

public class AssignStatementNode extends StatementNode
{
    public final VarExpr var;
    public final ExprNode expression;

    public AssignStatementNode(VarExpr var, ExprNode expression) {
        this.var = var;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return var.toString()+" = "+expression.toString();
    }

    @Override
    public double execute(Map<String, Double> vars) {
        return expression.eval(vars);
    }
}
