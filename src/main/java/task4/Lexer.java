package task4;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Лексический анализатор
 */
public class Lexer {

    /**
     * Входная строка
     */
    private final String str;
    /**
     * Текущая позиция во входной строке
     */
    private int index = 0;

    public Lexer(String str) {
        this.str = str;
    }

    /**
     * Попытка сопоставить текст, начиная с текущей позиции index, с
     * регулярным выражением.
     *
     * @param pattern регулярное выражение
     * @return -1, если если регулярное выражение не удалось найти в
     * текущей позиции; значение >= 0 - индекс первого символа,
     * следующего после найденной лексемы, соответствующей
     * регулярному выражению
     */
    private int match(Pattern pattern) {
        Matcher matcher = pattern.matcher(str);
        // Устанавливаем регион поиска - начиная с текущей позиции:
        matcher.region(index, str.length());
        if (matcher.lookingAt()) {
            // Да, в текущей позиции найдено регулярное выражение - возвращаем индекс символа _после_ найденной лексемы
            return matcher.end();
        } else {
            // Не найдено совпадения - возвращаем -1
            return -1;
        }
    }

    private Token matchNumber() {
        Pattern numberPattern = Pattern.compile("[0-9]+(\\.[0-9]*)?|\\.[0-9]+");
        int matched = match(numberPattern);
        if (matched < 0)
            return null;
        String numberText = str.substring(index, matched);
        return new Token(TokenType.NUMBER, numberText, index, matched);
    }
    private Token matchAnyVar()
    {
        Pattern varPattern = Pattern.compile("[a-z]+");
        int matched = match(varPattern);
        if(matched < 0)
            return null;
        String varText = str.substring(index,matched);
        return new Token(TokenType.VAR, varText,index, matched);
    }
    //private final Map<String, TokenType> SYMBOL_MAP = new HashMap<>();
    private final List<Node<Pattern, TokenType>> SYMBOLS = new LinkedList<>();

    {
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("+")), TokenType.ADD));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("-")), TokenType.SUB));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("*")), TokenType.MUL));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("/")), TokenType.DIV));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("!")), TokenType.EXCLAM));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("(")), TokenType.LPAR));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote(")")), TokenType.RPAR));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote(":=")), TokenType.ASSIGN));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote(";")), TokenType.SEP));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("==")), TokenType.EQ));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("sin")), TokenType.SIN));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("cos")), TokenType.COS));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("exp")), TokenType.EXP));
        SYMBOLS.add(new Node<>(Pattern.compile(Pattern.quote("^")), TokenType.POW));
        SYMBOLS.add(new Node<>(Pattern.compile("func [a-z]+[(]([a-z]+,)+[a-z][)]:[\\s\\S]+return[^;]+"), TokenType.FUNC_DEC));
        SYMBOLS.add(new Node<>(Pattern.compile("[a-z]+[(]([\\d]+,)+[\\d][)]"), TokenType.FUNC_EXE));
    }

    private Token matchAnySymbol() {
        for (Node<Pattern, TokenType> node : SYMBOLS) {

            TokenType value = node.getValue();
            int matched = match(node.getKey());
            if (matched < 0)
                continue;
            String symbolText = str.substring(index, matched);
            return new Token(value, symbolText, index, matched);
        }
        return null;
    }


    private Token matchSpaces() {
        int i = index;
        while (i < str.length()) {
            char ch = str.charAt(i);
            if (ch <= ' ') {
                i++;
            } else {
                break;
            }
        }
        if (i > index) {
            String spaces = str.substring(index, i);
            return new Token(TokenType.SPACES, spaces, index, i);
        } else {
            return null;
        }
    }

    /**
     * Получение лексемы, стоящей в текущей позиции.
     *
     * @return null, если в строке больше нет лексем
     */
    private Token matchAnyToken() throws ParseException {
        // Мы стоим в конце строки - больше нет лексем:
        if (index >= str.length())
            return null;
        // Перебираем все возможные типы лексем:
        Token spacesToken = matchSpaces();
        if (spacesToken != null)
            return spacesToken;
        Token numberToken = matchNumber();
        if (numberToken != null)
            return numberToken;
        Token symbolToken = matchAnySymbol();
        if (symbolToken != null)
            return symbolToken;
        Token varToken = matchAnyVar();
        if(varToken != null)
            return varToken;
        // Символ в текущей позиции не подходит ни к одной из возможных лексем - ошибка:
        throw new ParseException(
            "Unexpected character '" + str.charAt(index) + "'", index
        );
    }

    /**
     * Получение лексемы, стоящей в текущей позиции и перемещение текущей позиции дальше.
     *
     * @return null, если в строке больше нет лексем
     */
    public Token nextToken() throws ParseException {
        while (true) {
            Token token = matchAnyToken();
            if (token == null) {
                // Строка закончилась, больше нет лексем:
                return null;
            }
            // Перемещаем текущую позицию после найденной лексемы:
            index = token.to;
            if (token.type != TokenType.SPACES) {
                // Непробельную лексему возвращаем:
                return token;
            }
        }
    }

    public List<Token> getAllTokens() throws ParseException {
        List<Token> allTokens = new ArrayList<>();
        while (true) {
            Token token = nextToken();
            if (token == null)
                break;
            allTokens.add(token);
        }
        return allTokens;
    }
    private static class Node<K,V>
    {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
    static Function parseFunction(Token t) throws ParseException
    {
        if (t.type != TokenType.FUNC_DEC)
            throw new RuntimeException("Function was expected!");
        int startIndex = 0;
        String function = t.text;

        Pattern p = Pattern.compile("func\\s+");
        Matcher m = p.matcher(function);
        m.region(startIndex, function.length());
        if (!m.lookingAt())
            throw new ParseException("'func' was expected", startIndex+t.from);// TODO: 23.11.2017
        startIndex = m.end();
        //Scanning name
        p = Pattern.compile("[a-z]+");
        m = p.matcher(function);
        m.region(startIndex, function.length());
        if (!m.lookingAt())
            throw  new ParseException("Function name was expected", startIndex+t.from); // TODO: 23.11.2017
        String name = function.substring(startIndex, m.end());
        startIndex = m.end();
        //Scanning variables
        p = Pattern.compile("[(]([a-z]+,)+[a-z][)]:");
        m = p.matcher(function);
        m.region(startIndex, function.length());
        if (!m.lookingAt())
            throw new ParseException("Arguments were expected!", startIndex+t.from);
        String[] variables = function.substring(startIndex+1, m.end()-2).split(",");
        startIndex = m.end();
        //Scanning tokens;
        p = Pattern.compile("([\\s\\S]+)return([\\s\\S]+)");
        m = p.matcher(function);//.substring(startIndex, function.length()));
        //m.region(startIndex, function.length());
        if (!m.find(startIndex))
            throw new ParseException("Exception", startIndex+t.from);
        List<Token> code = new Lexer(m.group(1)).getAllTokens();
        List<Token> returnStatement = new Lexer(m.group(2)).getAllTokens();

        return new Function(variables, name, code, returnStatement);
    }
    static Double[] parseFuncCall(String func) throws ParseException {
        Pattern p = Pattern.compile("[a-z]+");
        Matcher m = p.matcher(func);
        String name = func.substring(0,m.end());
        if (Function.get(name)==null)
            throw new ParseException("Function wasn't found!",0); // TODO: 24.11.2017
        func = func.substring(m.end()+1, func.length()-1);
        return (Double[]) Arrays.stream(func.split(",")).map(Double::parseDouble).collect(Collectors.toList()).toArray();
    }
}
