package task6;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Runner {
    public static void main(String[] args) {
        String test = "1+2+3";
        ExprLexer lexer = new ExprLexer(CharStreams.fromString(test));
        ExprParser p = new ExprParser(new CommonTokenStream(lexer));
        ExprParser.ExprContext context = p.expr();

    }
}
