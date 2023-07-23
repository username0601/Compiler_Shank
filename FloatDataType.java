package lexer;

public class FloatDataType extends InterpreterDataType{

	private Double value;
	
	FloatDataType(double value){
		this.value = value;
	}
	
	FloatDataType(){
		this.value = null;
	}
	
	public double getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return Double.toString(this.value);
	}

	@Override
	public void fromString(String input) {
		this.value = Double.parseDouble(input); 	
	}

}
