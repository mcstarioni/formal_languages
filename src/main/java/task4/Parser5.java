package task4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Грамматический разбор грамматики
 * выражение ::= слагаемое (('+'|'-') слагаемое)*
 * слагаемое ::= множитель (('*'|'/') множитель)*
 * множитель ::= ('-')? ЧИСЛО | '(' выражение ')' ('!')?
 * с построением дерева разбора.
 */
public class Parser5
{
    /**
     * Список лексем
     */
    private final List<Token> tokens;
    /**
     * Индекс текущей лексемы
     */
    private int index = 0;
    public Parser5(List<Token> tokens)
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
    private Token matchAny(TokenType... types)
    {
        for (TokenType type : types)
        {
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
    private ExprNode matchMnozhitel() throws ParseException,EOSException
    {
        // В начале может стоять унарный минус:
        Token minus = match(TokenType.SUB);
        Token number = match(TokenType.NUMBER);
        Token var = match(TokenType.VAR);
        Token eos = match(TokenType.EOS);
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
            {
                if (match(TokenType.LPAR) != null)
                {
                    // Если это открывающая скобка, то вызываем разбор выражения в скобках:
                    ExprNode nested = matchComparison();
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
        if(eos != null)
        {
            throw new EOSException(result);
        }
        return result;
    }

//    private ExprNode matchVar() throws ParseException
//    {
//        Token var = match(TokenType.VAR);
//        if(var != null)
//        {
//            return var;
//        }
//    }

    /**
     * Метод для нетерминального символа 'слагаемое'.
     *
     * @return узел дерева, соответствующий слагаемому
     */
    private ExprNode matchSlagaemoe() throws ParseException,EOSException
    {
        // В начале должен быть множитель:
        ExprNode leftNode = null;
        try
        {
            leftNode = matchMnozhitel();
        }
        catch (EOSException eos)
        {
            throw new EOSException( eos.node);
        }
        while (true)
        {
            // Пока есть символ '*' или '/'...
            Token op = matchAny(TokenType.MUL, TokenType.DIV);
            if (op != null)
            {
                // Требуем после умножения/деления снова множитель:
                ExprNode rightNode;
                try
                {
                    rightNode = matchMnozhitel();
                }
                catch (EOSException eos)
                {
                    throw new EOSException(new BinOpExpr(leftNode,op,eos.node));
                }
                // Из двух множителей формируем дерево с двумя поддеревьями:
                leftNode = new BinOpExpr(leftNode, op, rightNode);
            } else
            {
                break;
            }
        }
        return null;
    }

    /**
     * Грамматический разбор выражения по грамматике
     * выражение ::= слагаемое (('+'|'-') слагаемое)*
     * слагаемое ::= множитель (('*'|'/') множитель)*
     * множитель ::= ('-')? ЧИСЛО | '(' выражение ')' ('!')?
     *
     * @return дерево разбора выражения
     */
    public ExprNode matchExpression() throws ParseException,EOSException
    {
        // В начале должно быть слагаемое:
        ExprNode leftNode = matchSlagaemoe();
        while (true)
        {
            // Пока есть символ '+' или '-'...
            Token op = matchAny(TokenType.ADD, TokenType.SUB);
            if (op != null)
            {
                // Требуем после плюса/минуса снова слагаемое:
                ExprNode rightNode = matchSlagaemoe();
                // Из двух слагаемых формируем дерево с двумя поддеревьями:
                leftNode = new BinOpExpr(leftNode, op, rightNode);
            } else
            {
                break;
            }
        }
        return leftNode;
    }
    public ExprNode matchComparison() throws ParseException,EOSException
    {
        ExprNode leftNode = matchExpression();
        while (true)
        {
            Token op = matchAny(TokenType.COMPARISON);
            if(op != null)
            {
                ExprNode rightNode = matchExpression();
                leftNode = new BinOpExpr(leftNode,op,rightNode);
            }
            else
            {
                break;
            }
        }
        return leftNode;
    }

    public StatementNode matchStatement() throws ParseException,EOSException
    {
        Token var = match(TokenType.VAR);
        Token assignment = match(TokenType.ASSIGNMENT);
        ExprNode expr = matchComparison();
        //Token eos = match(TokenType.EOS);
        if(var != null && assignment != null)
        {
            return new AssignStatement(var, expr);
        }
        else
        {
            throw new ParseException("Not a statement",0);
        }
    }
    public Program matchProgram() throws ParseException
    {
        Program p = new Program();
        do
        {
            StatementNode st = null;
            try
            {
                 st = matchStatement();
            }
            catch (EOSException es)
            {

            }
            if(st != null)
            {
                p.addStatement(st);
            }
            else
            {
                break;
            }
        }while (true);
        return p;
    }


    /**
     * Проверка грамматического разбора выражения
     */
    public void eval(Map<String,Double> vars,List<StatementNode> programm)
    {
        for(StatementNode s: programm)
        {
            //vars.replace(s.)
        }
    }
    public double eval(Map<String,Double> vars, ExprNode root)
    {
        return root.eval(vars);
    }
    public static void main(String[] args) throws ParseException {
        String expression = "x = -1.23 ; " +
                            "y = x*5 + 2;" ;
        //String expression = "(5 <> 5) == (2 <> 3)";
        Lexer lexer = new Lexer(expression);
        List<Token> allTokens = lexer.getAllTokens();
        Parser5 parser = new Parser5(allTokens);
        Program program = parser.matchProgram();
        //System.out.println(exprTreeRoot.toString());
        //HashMap<String,Double> arg = new HashMap<String, Double>();
        //arg.put("x",3.0);
        System.out.println(program.toString());
//        program.vars.
//                stream().
//                forEach(variable -> System.out.println(
//                        ((Variable)variable).getName()+ " : " +  ((Variable)variable).getValue()));
    }
}
