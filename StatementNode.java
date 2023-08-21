package shankCompiler;

public class StatementNode extends Node{

	private String statementName;
	
	StatementNode(String statementName){
		this.statementName = statementName;
	}
	
	@Override
	public String toString() {
		return this.statementName;
	}

}