package task4;

public abstract class StatementNode
{
    protected Program p;
    public void setProgram(Program p)
    {
        this.p = p;
    }
    public abstract double eval();
}
