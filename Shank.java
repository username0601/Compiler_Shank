package lexer;

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
		
		
		
//		try{
			List<String> lines = Files.readAllLines(filePath);
			ArrayList<Token> tokens = new ArrayList<>();;
			
			for(String line: lines) {
				tokens.addAll(lexer.lex(line));
//				System.out.println(tokens);
//				Parser parser = new Parser(tokens);
//				Node node = parser.parse();
//				System.out.println(node);
//				Interpreter interpreter = new Interpreter();
//				System.out.println(interpreter.resolve(node));
			}
			System.out.println(tokens);
			Parser parser = new Parser(tokens);
			System.out.println();
//			Node node = parser.parse();
//			System.out.println(node);
			
			Interpreter interpreter = new Interpreter();
			Interpreter.function.put("read", new Read());
			Interpreter.function.put("write", new Write());
			Interpreter.function.put("integerToReal", new IntegerToReal());
			Interpreter.function.put("realToInteger", new RealToInteger());
			Interpreter.function.put("squareRoot", new SquareRoot());
			Interpreter.function.put("getRandom", new GetRandom());
			
			ArrayList<InterpreterDataType> parameters = new ArrayList<InterpreterDataType>();
			parameters.add(new IntDataType(3));
			
			ArrayList<FunctionNode> functionNodes = new ArrayList<FunctionNode>();
			

//			Node node = parser.parse();
//			interperter.interpretFunction((FunctionNode)node, parameters);
			
			

			
			functionNodes.add((FunctionNode)(parser.parse()));
			FunctionNode second = (FunctionNode)(parser.parse());
			Interpreter.function.put(second.getFunctionName(), second);
			functionNodes.add(second);

			System.out.println(functionNodes.get(0));
			System.out.println(functionNodes.get(1));
			
			interpreter.interpretFunction(functionNodes.get(0), parameters);
			
//			
//			for(FunctionNode fc: functionNodes) {
//				interpreter.interpretFunction(fc, parameters);
//			}
			
						
//			Node node;
//			while((node = parser.parse()) != null) {
//				interpreter.interpretFunction((FunctionNode)node, parameters);
//				System.out.println(node);
//			}
			
			
//			for(String line : lines) {
//
//				for(Token token : lexer.lex(line)) {
//					System.out.print(token + " ");
//				}
//				System.out.print("\n");
//			}
//		}catch(Exception e) {
//			System.out.println(e);
//		}
		
	}
}
