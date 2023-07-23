package lexer;

import java.util.ArrayList;
import java.util.Random;

public class GetRandom extends BuiltInFunctionNode{

	private Random random = new Random();
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(ArrayList<InterpreterDataType> interpreterDataTypes) throws Exception {
		if(interpreterDataTypes.size() == 1) {
			if(interpreterDataTypes.get(0) instanceof IntDataType) {
				// generate int number from 0(inclusive) to 99(inclusive)
				interpreterDataTypes.set(0, new IntDataType(random.nextInt(99)));
			}
			// for new function-call defined passing by reference variable
			// its initial value would be null and will be assigned in here
			else if(interpreterDataTypes.get(0) == null) {
				interpreterDataTypes.set(0, new IntDataType(random.nextInt(99)));
			}else {
				throw new Exception("Build in function 'getRandom' error: passing by reference return data type should be float");
			}
		}else {
			throw new Exception("Build in function 'getRandom' error: wrong input size can't read");
		}
	}

	@Override
	public boolean getVariadic() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
