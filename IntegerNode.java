package shankCompiler;

public class IntegerNode extends Node{

	private int value;
	
	// for integer data type number
	IntegerNode(int value){
		this.value = value;
	}
	
	public int getInteger() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
	
}
