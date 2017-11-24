package task4;

public class EOSException extends Exception
{
    public final ExprNode node;
    EOSException(ExprNode node)
    {
        this.node = node;
    }
}
