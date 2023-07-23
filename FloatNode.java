package lexer;

public class FloatNode extends Node{
	
	private double value;
	
	// for float data type number
	FloatNode(double value){
		this.value = value;
	}
	
	public double getFloat() {
		return this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
	
	
}
