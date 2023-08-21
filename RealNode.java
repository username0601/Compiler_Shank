package shankCompiler;

public class RealNode extends Node{

	private Double value;
	private String dataType = "real";
	
	// for real data type variable
	RealNode(double value){
		this.value = value;
	}
	
	RealNode(){
		this.value = null;
	}
	
	public Double getValue() {
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
