package lexer;

public class VariableNode extends Node{
	
	private String name;
	private Node dataTypeNode;
	private boolean isConstant;
	
	VariableNode(String name){
		this.isConstant = false;
		this.name = name;
		this.dataTypeNode = null; 
	}
	
	//for variable node with no initial value
	VariableNode(String name, Node node){
		this.isConstant = false;
		this.name = name;
		this.dataTypeNode = node; 
	}
	
	//for variable node with initial value, most are constant
	VariableNode(String name, Node node, boolean isConstant){
		this.isConstant = isConstant;
		this.dataTypeNode = node; 
		this.name = name;
	}
	
	public Node getDataTypeNode() {
		return this.dataTypeNode;
	}
	
	public String getName() {
		return this.name;
	}
	
    public boolean isConstant() {
    	return this.isConstant;
    }
	
	@Override
	public String toString() {
		if(this.isConstant) {
			return this.name + "(" + this.dataTypeNode.toString() + ")";
		}else if(this.dataTypeNode == null) {
			return this.name + "(null for scope inside function variable)";
		}else {
			return this.dataTypeNode.getClass().getSimpleName() + ": " + name;
		}
	}

}
