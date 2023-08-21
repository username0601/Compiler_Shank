package shankCompiler;

import java.util.ArrayList;

public class FunctionNode extends CallableNode{
	
	private String name;
	private ArrayList<Node> parameters;
	private ArrayList<Node> constants;
	private ArrayList<Node> variables;
	private ArrayList<StatementNode> statements;
	
	FunctionNode(String name, ArrayList<Node> parameters, ArrayList<Node> constants, ArrayList<Node> variables,
			ArrayList<StatementNode> statements){
		this.name = name;
		this.parameters = parameters;
		this.constants = constants;
		this.variables = variables;
		this.statements = statements;
	}
	
	public ArrayList<Node> getParameters(){
		return this.parameters;
	}
	
	public ArrayList<Node> getConstants(){
		return this.constants;
	}
	
	public ArrayList<Node> getVariables(){
		return this.variables;
	}
	
	public ArrayList<StatementNode> getStatements(){
		return this.statements;
	}
	
	public String getFunctionName() {
		return this.name;
	}
	
	public String toString() {
		String function = "Function name: " + this.name + "\n";
		
		function += "Parameters:\n";
		for(Node node: this.parameters) {
			function += "\t" + node.toString() + "\n";
		}
		
		function += "Constants:\n";
		for(Node node: this.constants) {
			function += "\t" + node.toString() + "\n";
		}
		
		function += "Variables:\n";
		for(Node node: this.variables) {
			function += "\t" + node.toString() + "\n";
		}
		
		function += "Statements:\n";
		for(Node node: this.statements) {
			function += "\t" + node.toString() + "\n";
		}
		
		return function;
	}
	
	
}
