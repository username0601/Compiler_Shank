package lexer;

import java.util.ArrayList;

public class IfNode extends StatementNode{

	private BooleanExpressionNode booleanExpressionNode;
	private ArrayList<StatementNode> statements;
	// elsifNode could be null meaning 'else'
	private IfNode elsifNode;
	
	IfNode(BooleanExpressionNode booleanExpressionNode, ArrayList<StatementNode> statements, IfNode elsifNode){
		super("If:\n\t\t");
		this.booleanExpressionNode = booleanExpressionNode;
		this.statements = statements;
		this.elsifNode = elsifNode;
	}
	
	public IfNode getNext() {
		return this.elsifNode;
	}
	
	public BooleanExpressionNode getBoolean() {
		return this.booleanExpressionNode;
	}
	
	public ArrayList<StatementNode> getIfstatements(){
		return this.statements;
	}
	
	@Override
	public String toString() {
		String result = super.toString() + this.booleanExpressionNode + "\n\t\tStatements:";
		for(StatementNode statement: this.statements) {
			result += "\n\t\t" + statement;
		}
		// for 'if' only
		if(this.getNext() == null) {
			return result;
		}
		// for elsif
		else if(this.getNext().getBoolean() != null) {
			result += "elsif\n" + this.elsifNode;
		}
		// for else
		else if(this.getNext().getBoolean() == null) {
			result += "else\n" + this.elsifNode;
		}
		return result;
		
	}

}
