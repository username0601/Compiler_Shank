package lexer;

import java.util.ArrayList;

public class RepeatNode extends StatementNode{

	private BooleanExpressionNode booleanExrpessionNode;
	private ArrayList<StatementNode> statements;
	
	RepeatNode(BooleanExpressionNode booleanExrpessionNode, ArrayList<StatementNode> statements){
		super("Repeat:\n\t\t");
		this.booleanExrpessionNode = booleanExrpessionNode;
		this.statements = statements;
	}
	
	public BooleanExpressionNode getBoolean() {
		return this.booleanExrpessionNode;
	}
	
	public ArrayList<StatementNode> getRepeatStatements(){
		return this.statements;
	}

	@Override
	public String toString() {
		String result = super.toString() + "Statements:";
		for(StatementNode statement: this.statements) {
			result += "\n\t\t" + statement;
		}
		result += "\n\t\tUntil " + this.booleanExrpessionNode;
		return result;
	}

}
