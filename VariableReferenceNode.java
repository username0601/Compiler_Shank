package lexer;

public class VariableReferenceNode extends Node{

	private String name;
	private Node dataTypeNode;
	
	VariableReferenceNode(String name){
		this.name = name;
		this.dataTypeNode = null;
	}
	
	VariableReferenceNode(String name, Node dataTypeNode){
		this.name = name;
		this.dataTypeNode = dataTypeNode;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Node getDataTypeNode() {
		return this.dataTypeNode;
	}
	
	@Override
	public String toString() {
		return "Variable reference: " + this.name + (this.dataTypeNode == null ? "" : (" (" + 
				(this.dataTypeNode.getClass() == IntNode.class ? "int" : "real") + ")"));
	}

}
