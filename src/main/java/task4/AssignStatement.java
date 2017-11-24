package task4;

public class AssignStatement extends StatementNode
{
    public Token var;
    public ExprNode expression;
    public AssignStatement(Token var, ExprNode expression)
    {
        this.var = var;
        this.expression = expression;
    }

    @Override
    public double eval()
    {
        //Double d = expression.eval()
        return 0.0;
    }
    public String toString()
    {
        return var.text + " = " + expression.toString();
    }
}
