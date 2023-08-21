package shankCompiler;

public class StringDataType extends InterpreterDataType {

	private String value;
	
	StringDataType(String value){
		this.value = value;
	}
	
	StringDataType(){
		this.value = null;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}

	@Override
	public void fromString(String input) {
		this.value = input; 	
	}

}
