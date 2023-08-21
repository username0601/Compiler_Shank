package shankCompiler;

import java.util.ArrayList;

public class FunctionCallNode extends StatementNode{

	private String FunctionName;
	private ArrayList<Node> parameters;
	
	FunctionCallNode(String FunctionName, ArrayList<Node> parameters) {
		super(FunctionName + ":");
		this.FunctionName = FunctionName;
		this.parameters = parameters;
	}

	public String getFunctionName() {
		return this.FunctionName;
	}
	
	public ArrayList<Node> getParameters(){
		return this.parameters;
	}
	
	@Override
	public String toString() {
		String result =  super.toString();
		for(Node parameter : parameters) {
			result += " " + parameter;
		}
		result += "\n";
		
		return result;
	}
}
