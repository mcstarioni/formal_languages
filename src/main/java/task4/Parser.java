package task4;

import java.io.IOException;
import java.util.*;

/**
 * Грамматический разбор грамматики
 * выражение            ::=         слагаемое (('+'|'-') слагаемое)*
 * слагаемое            ::=         множитель (('*'|'/') множитель)*
 * множитель            ::=         ('-')? функция| ЧИСЛО |'(' выражение ')' ('!')?
 * функция     ::=         множитель
 * с построением дерева разбора.
 */
public class Parser
{

    /**
     * Список лексем
     */
    private final List<Token> tokens;
    /**
     * Индекс текущей лексемы
     */
    private int index = 0;
    private final TokenType[] highPriority = new TokenType[]{TokenType.MUL, TokenType.DIV,
        TokenType.POW, TokenType.EQ};

    private final TokenType[] lowPriority = new TokenType[]{TokenType.ADD, TokenType.SUB};
    //private final TokenType[] unaryOps = new TokenType[]{TokenType.SIN, TokenType.COS, TokenType.EXP};

    public Parser(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    /**
     * Проверка типа текущей лексемы.
     *
     * @param type предполагаемый тип лексемы
     * @return не null, если текущая лексема предполагаемого типа (при этом текущи индекс сдвигается на 1);
     * null, если текущая лексема другого типа
     */
    private Token match(TokenType type)
    {
        if (index >= tokens.size())
            return null;
        Token token = tokens.get(index);
        if (token.type != type)
            return null;
        index++;
        return token;
    }

    /**
     * Сообщение об ошибке с указанием текущей позиции в тексте.
     *
     * @param message текст сообщения
     */
    private void error(String message) throws ParseException
    {
        // Позиция ошибки в тексте
        int errorPosition;
        if (index >= tokens.size())
        {
            // Мы стоим в конце текста
            if (tokens.isEmpty())
            {
                // Лексем не было вообще - текст пустой; указываем на начало текста
                errorPosition = 0;
            } else
            {
                // Берем координату после последней лексемы
                errorPosition = tokens.get(tokens.size() - 1).to;
            }
        } else
        {
            // Берем координату текущей лексемы
            Token token = tokens.get(index);
            errorPosition = token.from;
        }
        throw new ParseException(message, errorPosition);
    }

    /**
     * Проверка типа текущей лексемы.
     *
     * @param types возможные типы лексем
     * @return не null, если текущая лексема одного из предполагаемых типов (при этом текущи индекс сдвигается на 1);
     * null, если текущая лексема другого типа
     */
    private Token matchAny(TokenType... types) {
        for (TokenType type : types) {
            Token matched = match(type);
            if (matched != null)
                return matched;
        }
        return null;
    }


    /**
     * Метод для нетерминального символа 'множитель'.
     *
     * @return узел дерева, соответствующий множителю
     */
    private ExprNode matchFactor() throws ParseException
    {
        // В начале может стоять унарный минус:
        Token minus = match(TokenType.SUB);
        Token number = match(TokenType.NUMBER);
        Token func = matchAny(TokenType.FUNC_EXE);
        Token var = (func == null)? match(TokenType.VAR): null;
        ExprNode result;
        if (number != null)
        {
            // Если это ЧИСЛО, то результат - узел для числа:
            result = new NumbExpr(number);
        } else
        {
            if (var != null)
            {
                result = new VarExpr(var);
            } else
            if (func != null)
            {
                Double[] d = Lexer.parseFuncCall(func.text);throw new UnsupportedOperationException();
                // TODO: 24.11.2017
            }
            else
            {
                if (match(TokenType.LPAR) != null)
                {
                    // Если это открывающая скобка, то вызываем разбор выражения в скобках:
                    ExprNode nested = matchExpression();
                    if (match(TokenType.RPAR) == null)
                    {
                        error("Missing ')'");
                    }
                    result = nested;
                } else
                {
                    // Иначе ошибка - других вариантов кроме числа и скобки быть не может:
                    error("Number or '(' expected");
                    return null;
                }
            }
        }

        // В конце может стоять факториал:
        Token factorial = match(TokenType.EXCLAM);
        if (factorial != null)
        {
            result = new UnOpExpr(factorial, result);
        }
        if (minus != null)
        {
            result = new UnOpExpr(minus, result);
        }
        return result;
    }

    private Token matchVar() throws ParseException
    {
        return match(TokenType.VAR);
    }

    /**
     * Метод для нетерминального символа 'слагаемое'.
     *
     * @return узел дерева, соответствующий слагаемому
     */
    private ExprNode matchTerm() throws ParseException
    {
        // В начале должен быть множитель:
        ExprNode leftNode = matchFactor();
        while (true)
        {
            // Пока есть символ '*' или '/' или '=='...
            Token op = matchAny(highPriority);
            if (op != null)
            {
                // Требуем после умножения/деления снова множитель:
                ExprNode rightNode = matchFactor();
                // Из двух множителей формируем дерево с двумя поддеревьями:
                leftNode = new BinOpExpr(leftNode, op, rightNode);
            } else
            {
                break;
            }
        }
        return leftNode;
    }

    /**
     * Грамматический разбор выражения по грамматике
     * выражение ::= слагаемое (('+'|'-') слагаемое)*
     * слагаемое ::= множитель (('*'|'/') множитель)*
     * множитель ::= ('-')? ЧИСЛО | '(' выражение ')' ('!')?
     *
     * @return дерево разбора выражения
     */
    public ExprNode matchExpression() throws ParseException
    {
        // В начале должно быть слагаемое:
        ExprNode leftNode = matchTerm();
        while (true)
        {
            // Пока есть символ '+' или '-'...
            Token op = matchAny(lowPriority);
            if (op != null)
            {
                // Требуем после плюса/минуса снова слагаемое:
                ExprNode rightNode = matchTerm();
                // Из двух слагаемых формируем дерево с двумя поддеревьями:
                leftNode = new BinOpExpr(leftNode, op, rightNode);
            } else
            {
                break;
            }
        }
        return leftNode;
    }
    public void matchFunction() throws ParseException
    {

    }

    public AssignStatementNode matchStatement() throws ParseException {
        Token t;
        while ((t = match(TokenType.FUNC_DEC)) != null)
        {
            Function.addFunction(Lexer.parseFunction(t));
            if (match(TokenType.SEP) == null)
                throw new ParseException("; was expected", tokens.get(index).from);
        }
        Token var = matchVar();
        if (var != null) {
            match(TokenType.ASSIGN);
            ExprNode rightNode = matchExpression();
            return new AssignStatementNode(new VarExpr(var), rightNode);
        }
        else
            throw new ParseException("Unexpected error!", 0);// TODO: 23.11.2017
    }
    public List<AssignStatementNode> matchProgram() throws ParseException {
        List<AssignStatementNode> nodes = new LinkedList<>();
        AssignStatementNode n;
        while (index < tokens.size()-1) {
            n = matchStatement();
            nodes.add(n);
            Token t = match(TokenType.SEP);
            if (t == null)
                throw new ParseException("; is expected", index);
        }
        return nodes;
    }

    public static Map<String, Double> eval(List<AssignStatementNode> nodes)
    {
        Map<String, Double> vars = new HashMap<>();
        for (AssignStatementNode node: nodes) {
            double value = node.execute(vars);
            String var = node.var.var.text;
            if (vars.containsKey(var))
            {
                vars.replace(var, value);
            }
            else
                vars.put(var, value);
        }
        return vars;
    }
    public Map<String, Double> execute()
    {
        return eval(matchProgram());
    }
    public static Map<String, Double> compile(String code) throws ParseException
    {

        Lexer lexer = new Lexer(code);
        List<Token> allTokens = lexer.getAllTokens();
        allTokens.forEach(System.out::println);

        Parser parser = new Parser(allTokens);
        return parser.execute();
    }
    public static void main(String[] args) throws ParseException, IOException {
        String expression = "func f(a,b,c): a:=1; b:= 2; return a+b; f := f(1,2,3);";
        compile(expression);

    }

}
