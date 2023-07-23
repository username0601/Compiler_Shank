package lexer;

public class MathOpNode extends Node{

	public enum MathOp {ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO}
	
	private MathOp mathOp;
	private Node left;
	private Node right;
	
	MathOpNode(char op, Node left, Node right){
		
		if(op == '+') {
			this.mathOp = MathOp.ADD;
		}else if(op == '-') {
			this.mathOp = MathOp.SUBTRACT;
		}else if(op == '*') {
			this.mathOp = MathOp.MULTIPLY;
		}else if(op == '/'){
			this.mathOp = MathOp.DIVIDE;
		}else if(op == '%'){
			this.mathOp = MathOp.MODULO;
		}
		
		this.left = left;
		this.right = right;
	}
	
	public MathOp getOp() {
		return this.mathOp;
	}
	
	public Node getLeft() {
		return this.left;
	}
	
	public Node getRight() {
		return this.right;
	}
	
	@Override
	public String toString() {
		return "(" + this.left + " " + this.mathOp.toString() + " " + this.right + ")";
	}

	
	
}
