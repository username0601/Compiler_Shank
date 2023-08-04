package lexer;

public class CharDataType extends InterpreterDataType{

	private String value;
	
	CharDataType(String value){
		this.value = value;
	}
	
	CharDataType(){
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
