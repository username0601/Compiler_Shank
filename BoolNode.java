package shankCompiler;

public class BoolNode extends Node{

	private Boolean value;
	private String dataType = "boolean";
	
	// for boolean data type variable
	BoolNode(boolean value){
		this.value = value;
	}
	
	BoolNode(){
		this.value = null;
	}
	
	public Boolean getValue() {
		return this.value;
	}
	
	public String getDataType() {
		return this.dataType;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(this.value);
	}

}
