package lexer;

public class AssignmentNode extends StatementNode{

	private VariableReferenceNode variableReferenceNode;
	private Node expressionNode;
	
	AssignmentNode(VariableReferenceNode variableReferenceNode, Node expressionNode){
		super("Assignment:\n\t\t");
		this.variableReferenceNode = variableReferenceNode;
		this.expressionNode = expressionNode;
	}
	
	@Override
	public String toString() {
		return super.toString() + this.variableReferenceNode + "\n\t\tAssigns to\n\t\t" + this.expressionNode + "\n";
	}

}
