package task6;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Runner {
    public static void main(String[] args) {
        String test = "1+2+3";
        firstLexer lexer = new firstLexer(CharStreams.fromString(test));
        firstParser p = new firstParser(new CommonTokenStream(lexer));
        firstParser.ExprContext context = p.expr();

    }
}
