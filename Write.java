package shankCompiler;

import java.util.ArrayList;

public class Write extends BuiltInFunctionNode{
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(ArrayList<InterpreterDataType> interpreterDataTypes) throws Exception {
		for (InterpreterDataType interpreterDataType: interpreterDataTypes){
			System.out.print(interpreterDataType + " ");
        }
		System.out.println();
	}

	@Override
	public boolean getVariadic() {
		return true;
	}

}
