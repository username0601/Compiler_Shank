package lexer;

import java.util.ArrayList;

public class WhileNode extends StatementNode{

	private BooleanExpressionNode booleanExpressionNode;
	private ArrayList<StatementNode> statements;
	
	WhileNode(BooleanExpressionNode booleanExpressionNode, ArrayList<StatementNode> statements){
		super("While:\n\t\t");
		this.booleanExpressionNode = booleanExpressionNode;
		this.statements = statements;
	}
	
	@Override
	public String toString() {
		String result = super.toString() + this.booleanExpressionNode + "\n\t\tStatements:";
		for(StatementNode statement: this.statements) {
			result += "\n\t\t" + statement;
		}
		return result;
	}
	
}
