package lexer;

import java.util.ArrayList;

public class SquareRoot extends BuiltInFunctionNode{
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(ArrayList<InterpreterDataType> interpreterDataTypes) throws Exception {
		if(interpreterDataTypes.size() == 2) {
			if(interpreterDataTypes.get(1) instanceof FloatDataType) {
				// don't know whether the input data is integer or float point number
				interpreterDataTypes.set(1, new FloatDataType( interpreterDataTypes.get(0) instanceof IntDataType ?
						(((IntDataType)(interpreterDataTypes.get(0))).getValue() * 
								((IntDataType)(interpreterDataTypes.get(0))).getValue())  :
						(((FloatDataType)(interpreterDataTypes.get(0))).getValue() * 
								((FloatDataType)(interpreterDataTypes.get(0))).getValue()) ));
			}
			// for new function-call defined passing by reference variable
			// its initial value would be null and will be assigned in here
			else if(interpreterDataTypes.get(1) == null) {
				interpreterDataTypes.set(1, new FloatDataType( interpreterDataTypes.get(0) instanceof IntDataType ?
						(((IntDataType)(interpreterDataTypes.get(0))).getValue() * 
								((IntDataType)(interpreterDataTypes.get(0))).getValue())  :
						(((FloatDataType)(interpreterDataTypes.get(0))).getValue() * 
								((FloatDataType)(interpreterDataTypes.get(0))).getValue()) ));
			}
			// input data could be integer or float, but the return data type must be float
			else {
				throw new Exception("Build in function 'squareRoot' error: wrong output data type, can not convert");
			}
		}else {
			throw new Exception("Build in function 'squareRoot' error: wrong input size can't read");
		}
	}

	@Override
	public boolean getVariadic() {
		// TODO Auto-generated method stub
		return false;
	}

}
