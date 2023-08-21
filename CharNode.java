package shankCompiler;

public class CharNode extends Node {

	private String value;
	private String dataType = "char";
	
	// for real data type variable
	CharNode(String value){
		this.value = value;
	}
	
	CharNode(){
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
