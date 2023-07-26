package lexer;

import java.util.ArrayList;

public class ForNode extends StatementNode{

	private VariableReferenceNode variableReferenceNode;
	private Node startNode;
	private Node endNode;
	private ArrayList<StatementNode> statements;
	
	ForNode(VariableReferenceNode variableReferenceNode, Node startNode, Node endNode, ArrayList<StatementNode> statements){
		super("For:\n\t\t");
		this.variableReferenceNode = variableReferenceNode;
		this.startNode = startNode;
		this.endNode = endNode;
		this.statements = statements;
	}
	
	public VariableReferenceNode getVariableReferenceNode() {
		return this.variableReferenceNode;
	}
	
	public Node getStartNode() {
		return this.startNode;
	}
	
	public Node getEndNode() {
		return this.endNode;
	}
	
	public ArrayList<StatementNode> getForStatements(){
		return this.statements;
	}
	
	@Override
	public String toString() {
		String result = super.toString();
		result += this.variableReferenceNode + " from " + this.startNode + " to " + this.endNode + "\n\t\tStatements:";
		for(StatementNode statement: this.statements) {
			result += "\n\t\t" + statement;
		}
		return result;
	}

}
