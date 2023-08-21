package shankCompiler;

public class StringNode extends Node{

	private String value;
	private String dataType = "string";
	
	// for real data type variable
	StringNode(String value){
		this.value = value;
	}
	
	StringNode(){
		this.value = null;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getDataType(){
		return this.dataType;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
