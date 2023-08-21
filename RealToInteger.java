package shankCompiler;

import java.util.ArrayList;

public class RealToInteger extends BuiltInFunctionNode{

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(ArrayList<InterpreterDataType> interpreterDataTypes) throws Exception {
		if(interpreterDataTypes.size() == 2) {
			if((interpreterDataTypes.get(0) instanceof FloatDataType) &&
					(interpreterDataTypes.get(1) instanceof IntDataType)) {
				interpreterDataTypes.set(1, new IntDataType((int)((FloatDataType)(interpreterDataTypes.get(0))).getValue()));
			}
			// for new function-call defined passing by reference variable
			// its initial value would be null and will be assigned in here
			else if((interpreterDataTypes.get(0) instanceof FloatDataType) &&
					(interpreterDataTypes.get(1) == null)) {
				interpreterDataTypes.set(1, new IntDataType((int)((FloatDataType)(interpreterDataTypes.get(0))).getValue()));
			}else {
				throw new Exception("Build in function 'realToInteger' error: wrong input data type, can not convert");
			}
		}else {
			throw new Exception("Build in function 'realToInteger' error: wrong input size can't read");
		}
	}

	@Override
	public boolean getVariadic() {
		// TODO Auto-generated method stub
		return false;
	}

}
