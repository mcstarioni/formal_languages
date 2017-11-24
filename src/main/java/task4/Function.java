package task4;

import java.util.*;

public class Function {
    final String[] variables;
    final String name;
    final List<Token> code;
    final List<Token> returns;

    public Function(String[] variables, String name, List<Token> code, List<Token> returns) {
        this.variables = variables;
        this.name = name;
        this.code = code;
        this.returns = returns;
    }

    public double execute(double... vars) throws ParseException
    {
        if (variables.length != vars.length)
            throw new ParseException("Variable error", 0);// TODO: 23.11.2017 DO!
        List<Token> templateList = genAssignment();
        templateList.addAll(new LinkedList<>(code));
        Parser parser = new Parser(templateList);
        Map<String, Double> map = parser.execute();

        return 0d;
    }

    public List<Token> genAssignment(double... val) {
        List<Token> tokens = new LinkedList<>();
        for (int i = 0; i < val.length; i++) {
            tokens.add(new Token(TokenType.VAR, variables[i], 0,0));
            tokens.add(new Token(TokenType.ASSIGN, ":=", 0,0));
            tokens.add(new Token(TokenType.NUMBER, String.valueOf(val[i]), 0,0));
            tokens.add(new Token(TokenType.SEP,";",0,0));
        }
        return tokens;
    }

    public static List<Function> functions = new LinkedList<>();
    public static void addFunction(Function f)
    {
        if (get(f.name) == null)
            functions.add(f);
    }
    public static Function get(String name)
    {
        return functions.stream().filter(fun-> Objects.equals(fun.name, name)).findFirst().orElse(null);
    }
}
