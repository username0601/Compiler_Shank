package lexer;

public class BooleanExpressionNode extends Node{

	private Node leftExpression;
	private Node rightExpression;
	private String condition;
	
	BooleanExpressionNode(Node leftExpression, Node rightExpression, String condition){
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		return "Condition: " + this.leftExpression + " " + this.condition + " " + this.rightExpression;
	}

}
