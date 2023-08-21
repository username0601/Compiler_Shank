package shankCompiler;

public class BooleanExpressionNode extends Node{

	private Node leftExpression;
	private Node rightExpression;
	private String condition;
	
	BooleanExpressionNode(Node leftExpression, Node rightExpression, String condition){
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
		this.condition = condition;
	}
	
	public Node getLeftExpression() {
		return this.leftExpression;
	}
	
	public Node getRightExpression() {
		return this.rightExpression;
	}
	
	public String getCondtion() {
		return this.condition;
	}
	
	@Override
	public String toString() {
		return "Condition: " + this.leftExpression + " " + this.condition + " " + this.rightExpression;
	}

}
