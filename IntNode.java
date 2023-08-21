package shankCompiler;

public class IntNode extends Node{

	private Integer value;
	private String dataType = "int";
	
	// for int data type variable
	IntNode(int value){
		this.value = value;
	}
	
	IntNode(){
		this.value = null;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
	public String getDataType() {
		return this.dataType;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
