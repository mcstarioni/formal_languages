package task6;// Generated from first.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link firstParser}.
 */
public interface firstListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link firstParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(firstParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link firstParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(firstParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link firstParser#add}.
	 * @param ctx the parse tree
	 */
	void enterAdd(firstParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by {@link firstParser#add}.
	 * @param ctx the parse tree
	 */
	void exitAdd(firstParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by {@link firstParser#mul}.
	 * @param ctx the parse tree
	 */
	void enterMul(firstParser.MulContext ctx);
	/**
	 * Exit a parse tree produced by {@link firstParser#mul}.
	 * @param ctx the parse tree
	 */
	void exitMul(firstParser.MulContext ctx);
}