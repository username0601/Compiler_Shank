package lexer;

public class BoolDataType extends InterpreterDataType{

	private Boolean value;
	
	BoolDataType(boolean value){
		this.value = value;
	}
	
	BoolDataType(){
		this.value = null;
	}
	
	public Boolean getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public void fromString(String input) {
		this.value = Boolean.parseBoolean(input);
	}

}
