package shankCompiler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Shank {
	public static void main(String[] args) throws Exception{
		
		//throw exception if file name invalid
		if(args.length != 1) {
			throw new Exception("Please provide only one file name");
		}
		
		String fileName = args[0];
		Path filePath = Paths.get(fileName);
		
		Lexer lexer = new Lexer();
		
		List<String> lines = Files.readAllLines(filePath);
		ArrayList<Token> tokens = new ArrayList<>();
			
		for(String line: lines) {
			tokens.addAll(lexer.lex(line));
		}
		System.out.println(tokens);
		Parser parser = new Parser(tokens);
		System.out.println();
		
		// add build-in functions in the Interpreter for future calls
		Interpreter.function.put("read", new Read());
		Interpreter.function.put("write", new Write());
		Interpreter.function.put("integerToReal", new IntegerToReal());
		Interpreter.function.put("realToInteger", new RealToInteger());
		Interpreter.function.put("squareRoot", new SquareRoot());
		Interpreter.function.put("getRandom", new GetRandom());
			
		ArrayList<InterpreterDataType> parameters = new ArrayList<InterpreterDataType>();
		parameters.add(new IntDataType(3));

		ArrayList<FunctionNode> functionNodes = new ArrayList<FunctionNode>();
		functionNodes.add((FunctionNode)(parser.parse()));
		
		Node funcNode;
		while(Parser.tokens.size() > 0 && (funcNode = parser.parse()) != null) {
			functionNodes.add((FunctionNode)funcNode);
			Interpreter.function.put(((FunctionNode)funcNode).getFunctionName(), (FunctionNode)funcNode);
		}
		
		SemanticAnalysis.checkAssignment(functionNodes);
		
		Interpreter.interpretFunction(functionNodes.get(0), parameters);
	}
}