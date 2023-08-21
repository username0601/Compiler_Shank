package shankCompiler;

import java.util.ArrayList;

public abstract class BuiltInFunctionNode extends CallableNode {
	
	@Override
	public abstract String toString();
	
	public abstract void execute(ArrayList<InterpreterDataType> interpreterDataTypes) throws Exception;

	public abstract boolean getVariadic();
	
}
