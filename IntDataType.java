package lexer;

public class IntDataType extends InterpreterDataType{

	private Integer value;
	
	IntDataType(int value){
		this.value= value;
	}
	
	IntDataType(){
		this.value = null;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.value);
	}

	@Override
	public void fromString(String input) {
		this.value = Integer.parseInt(input);
	}

}
