package task4;

import java.util.ArrayList;
import java.util.List;

public class Program
{
    List<Variable> vars = new ArrayList<>();
    List<StatementNode> statements = new ArrayList<>();
    public void addStatement(StatementNode st)
    {
        statements.add(st);
        st.setProgram(this);
    }
    public void eval()
    {
        statements.
                stream()
                .forEach(statementNode ->
                        vars.
                                stream().
                                filter(variable ->
                                        variable.getName() == ((AssignStatement) statementNode).var.text).
                                forEach(variable ->
                                        variable.setValue(statementNode.eval())));
    }
    public String toString()
    {
        String result = "";
        for(StatementNode st : statements)
        {
            result += statements.toString();
        }
        return result;
    }
}
