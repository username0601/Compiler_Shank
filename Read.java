package lexer;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Read extends BuiltInFunctionNode{
	
	private Scanner scanner;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(ArrayList<InterpreterDataType> interpreterDataTypes) throws Exception {
		scanner = new Scanner(System.in);
        for (int i = 0; i < interpreterDataTypes.size(); i++) {
			System.out.print("Enter your data: ");
        	String data = scanner.nextLine();
        	if(Pattern.matches("-?\\d+\\.\\d+", data)) {
        		interpreterDataTypes.set(i, new FloatDataType(Double.parseDouble(data))); 
        	}else if(Pattern.matches("-?\\d+", data)) {
        		interpreterDataTypes.set(i, new IntDataType(Integer.parseInt(data))); 
        	}else {
        		throw new Exception("Build in function 'read' error: wrong input can't read");
        	}
		}
	}

	@Override
	public boolean getVariadic() {
		return true;
	}


}
