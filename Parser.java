package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class Parser {

	private ArrayList<Token> tokens;
	
	Parser(ArrayList<Token> tokens){
		this.tokens = tokens;
	}
	
	private Token matchAndRemove(TokenType tokenType) {
		if(this.tokens.get(0).getTokenType() == tokenType) {
			return this.tokens.remove(0);
		}else {
			return null;
		}
	}
	
	public Node parse() throws Exception {
//		Node expression = expression();
//		this.matchAndRemove(TokenType.EndOfLine);
//		return expression;
		return functionDefinition();
	}
	
	private Node functionDefinition() throws Exception {
		if(this.matchAndRemove(TokenType.DEFINE) != null) {
			return function();
		}else {
			return null;
		}
	}
	
	private Node function() throws Exception {
		Token identifier = this.matchAndRemove(TokenType.IDENTIFIER);
		if( identifier != null) {
			String functionName = identifier.getValue();
			if (this.matchAndRemove(TokenType.LeftParenthesis) != null) {
				ArrayList<Node> parameters = variableDeclaration();
				
				if (this.matchAndRemove(TokenType.RightParenthesis) != null) {
					this.matchAndRemove(TokenType.EndOfLine);
					ArrayList<Node> constants = constants();
					ArrayList<Node> variables = variables();
					ArrayList<StatementNode> body = bodyFunction();
					return new FunctionNode(functionName, parameters, constants, variables, body);
				}else {
					throw new Exception("Right parenthesis should follow after parameter declaration");
				}
			}else {
				throw new Exception("Left parenthesis should follow after 'identifier'");
			}
		}else {
			throw new Exception("Identifier should follow after 'define'");
		}
	}
	
	private ArrayList<Node> variableDeclaration() throws Exception{
		ArrayList<Node> variableNode = new ArrayList<>();
		ArrayList<String> variableName = new ArrayList<>();
		Token identifier;
		do {
			// for Passing by Reference variable, scope outside the function 
			if(this.matchAndRemove(TokenType.VAR) != null) {
				while((identifier = this.matchAndRemove(TokenType.IDENTIFIER)) != null) {
					variableName.add(identifier.getValue());
					if(this.matchAndRemove(TokenType.COMMA) != null) {
						continue;
					}else if(this.matchAndRemove(TokenType.COLON) != null) {
						break;
					}else {
						throw new Exception("variable name declaration invalid");
					}
				}
				if(this.matchAndRemove(TokenType.INTEGER) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableReferenceNode(name, new IntNode()));
					}
				}else if(this.matchAndRemove(TokenType.REAL) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableReferenceNode(name, new RealNode()));
					}
				}else if(this.matchAndRemove(TokenType.BOOL) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new BoolNode()));
					}
				}else if(this.matchAndRemove(TokenType.CHAR) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new CharNode()));
					}
				}else if(this.matchAndRemove(TokenType.STRING) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new StringNode()));
					}
				}else {
					throw new Exception("variable data type declaration invalid " + this.tokens.get(0) + " " + variableName);
				}
				variableName.clear();
			}
			// for Passing by value variable, scope inside the function 
			else {
				while((identifier = this.matchAndRemove(TokenType.IDENTIFIER)) != null) {
					variableName.add(identifier.getValue());
					if(this.matchAndRemove(TokenType.COMMA) != null) {
						continue;
					}else if(this.matchAndRemove(TokenType.COLON) != null) {
						break;
					}else {
						throw new Exception("variable name declaration invalid");
					}
				}
				if(this.matchAndRemove(TokenType.INTEGER) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new IntNode()));
					}
				}else if(this.matchAndRemove(TokenType.REAL) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new RealNode()));
					}
				}else if(this.matchAndRemove(TokenType.BOOL) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new BoolNode()));
					}
				}else if(this.matchAndRemove(TokenType.CHAR) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new CharNode()));
					}
				}else if(this.matchAndRemove(TokenType.STRING) != null) {
					for(String name: variableName) {
						variableNode.add(new VariableNode(name, new StringNode()));
					}
				}else {
					throw new Exception("variable data type declaration invalid " + this.tokens.get(0) + " " + variableName);
				}
				variableName.clear();
			}
		}while(this.matchAndRemove(TokenType.SEMICOLON) != null);
		// ???if the parameter declaration go to another line???
//		this.matchAndRemove(TokenType.EndOfLine);
		return variableNode;
	}
	
	
	private ArrayList<Node> constants() throws Exception{
		Token constant = this.matchAndRemove(TokenType.CONSTANTS);
		if(constant != null) {
			this.matchAndRemove(TokenType.EndOfLine);
			ArrayList<Node> constantNode = processConstants();
			if(constantNode.isEmpty()) {
				throw new Exception("need to declare constant after 'constant'");
			}else {
				return constantNode;
			}
		}else {
			return null;
		}
	}
	
	private ArrayList<Node> processConstants() throws Exception{
		ArrayList<Node> constantNode = new ArrayList<>();
		String constantName;
		Token token ;
		while((token= this.matchAndRemove(TokenType.IDENTIFIER)) != null) {
			constantName = token.getValue();
			if(this.matchAndRemove(TokenType.EQUAL) != null) {
				if((token = this.matchAndRemove(TokenType.NUMBER)) != null) {
					if(token.getValue().contains(".")) {
						constantNode.add(new VariableNode(constantName, new RealNode(Double.parseDouble(token.getValue())), true));
					}else {
						constantNode.add(new VariableNode(constantName, new IntNode(Integer.parseInt(token.getValue())), true));
					}
					this.matchAndRemove(TokenType.EndOfLine);
				}else if((token = this.matchAndRemove(TokenType.TRUE)) != null)  {
					constantNode.add(new VariableNode(constantName, new BoolNode(true), true));
				}else if((token = this.matchAndRemove(TokenType.FALSE)) != null)  {
					constantNode.add(new VariableNode(constantName, new BoolNode(false), true));
				}else if((token = this.matchAndRemove(TokenType.CHAR)) != null)  {
					constantNode.add(new VariableNode(constantName, new CharNode(token.getValue()), true));
				}else if((token = this.matchAndRemove(TokenType.STRING)) != null)  {
					constantNode.add(new VariableNode(constantName, new StringNode(token.getValue()), true));
				}
				else {
					throw new Exception("Parser error: constant initialization invalid");
				}
			}else {
				throw new Exception("single equal character need to follow after the identifier");
			}
		}
		return constantNode;
	}
	
	private ArrayList<Node> variables() throws Exception{
		Token variable = this.matchAndRemove(TokenType.VARIABLES);
		if(variable != null) {
			this.matchAndRemove(TokenType.EndOfLine);
			ArrayList<Node> variableNode = processVariables();
			if(variableNode.isEmpty()) {
				throw new Exception("need to declare variable after 'variables'");
			}else {
				return variableNode;
			}
		}else {
			return null;
		}
	}
	
	private ArrayList<Node> processVariables() throws Exception{
		ArrayList<Node> variableNode = new ArrayList<>();
		ArrayList<String> variableName = new ArrayList<>();
		Token identifier = this.matchAndRemove(TokenType.IDENTIFIER);
		do {
			while(identifier != null) {
				variableName.add(identifier.getValue());
				if(this.matchAndRemove(TokenType.COMMA) != null) {
					identifier = this.matchAndRemove(TokenType.IDENTIFIER);
					continue;
				}else if(this.matchAndRemove(TokenType.COLON) != null) {
					break;
				}else {
					throw new Exception("variable name declaration invalid " + identifier);
				}
			}
			if(this.matchAndRemove(TokenType.INTEGER) != null) {
				for(String name: variableName) {
					variableNode.add(new VariableNode(name, new IntNode()));
				}
			}else if(this.matchAndRemove(TokenType.REAL) != null) {
				for(String name: variableName) {
					variableNode.add(new VariableNode(name, new RealNode()));
				}
			}else if(this.matchAndRemove(TokenType.BOOL) != null) {
				for(String name: variableName) {
					variableNode.add(new VariableNode(name, new BoolNode()));
				}
			}else if(this.matchAndRemove(TokenType.CHAR) != null) {
				for(String name: variableName) {
					variableNode.add(new VariableNode(name, new CharNode()));
				}
			}else if(this.matchAndRemove(TokenType.STRING) != null) {
				for(String name: variableName) {
					variableNode.add(new VariableNode(name, new StringNode()));
				}
			}else {
				throw new Exception("variable data type declaration invalid " + this.tokens.get(0));
			}
			if(this.matchAndRemove(TokenType.EndOfLine) != null) {
				identifier = this.matchAndRemove(TokenType.IDENTIFIER);
			}else {
				throw new Exception("need to start on a new line for another variable declaratino or body function");
			}
			variableName.clear();
		}while(identifier != null);
		
		return variableNode;
	}
	
	private ArrayList<StatementNode> bodyFunction() throws Exception{
		if(this.matchAndRemove(TokenType.BEGIN) != null) {
			if(this.matchAndRemove(TokenType.EndOfLine) != null) {
				ArrayList<StatementNode> statements = statements();
				if(this.matchAndRemove(TokenType.END) != null) {
					if(this.matchAndRemove(TokenType.EndOfLine) != null) {
						return statements;
					}else {
						throw new Exception("need to have nothing after 'end'");
					}
				}else {
					throw new Exception("need to have 'end' to end function body");
				}
			}else {
				throw new Exception("need to start a new line after 'begin'");
			}
		}else {
			throw new Exception("function body should start on 'begin'");
		}
	}
	
	private ArrayList<StatementNode> statements() throws Exception{
		ArrayList<StatementNode> statements = new ArrayList<>();
		StatementNode statement;
		while((statement = statement()) != null) {
			statements.add(statement);
			this.matchAndRemove(TokenType.EndOfLine);
		}
		return statements;
	}
	
	private StatementNode statement() throws Exception{
		
		if(this.tokens.get(0).getTokenType() == TokenType.IDENTIFIER && this.tokens.get(1).getTokenType() == TokenType.ASSIGNMENT) {
			return assignments();
		}else if(this.matchAndRemove(TokenType.WHILE) != null) {
			return whileStatement();
		}else if(this.matchAndRemove(TokenType.FOR) != null) {
			return forStatement();
		}else if(this.matchAndRemove(TokenType.IF) != null) {
			return ifStatement();
		}else if(this.matchAndRemove(TokenType.REPEAT) != null) {
			return repeatStatement();
		}else if(this.tokens.get(0).getTokenType() == TokenType.IDENTIFIER) {
			return functionCallStatement();
		}else {
			return null;
		}
	}
	
	private FunctionCallNode functionCallStatement() throws Exception{
		String functionName = this.matchAndRemove(TokenType.IDENTIFIER).getValue();
		ArrayList<Node> parameters = new ArrayList<>();
		while(this.tokens.get(0).getTokenType() == TokenType.VAR ||
				this.tokens.get(0).getTokenType() == TokenType.IDENTIFIER || 
				this.tokens.get(0).getTokenType() == TokenType.NUMBER || 
				this.tokens.get(0).getTokenType() == TokenType.TRUE ||
				this.tokens.get(0).getTokenType() == TokenType.FALSE ||
				this.tokens.get(0).getTokenType() == TokenType.CHARCONTENT ||
				this.tokens.get(0).getTokenType() == TokenType.STRINGCONTENT ) {
			// identifier for passing by reference variable or passing by value variable
			Token token;
			if(this.matchAndRemove(TokenType.VAR) != null) {
				if((token = this.matchAndRemove(TokenType.IDENTIFIER)) != null) {
					parameters.add(new VariableReferenceNode(token.getValue()));
				}else {
					throw new Exception("name of the passing by reference must follow after the 'var'");
				}
			}
			// the identifier is a passing by value variable which could be variable or constant
			else if((token = this.matchAndRemove(TokenType.IDENTIFIER)) != null) {
				parameters.add(new VariableNode(token.getValue()));
			}else if((token = this.matchAndRemove(TokenType.NUMBER)) != null){
				String value = token.getValue();
				if(value.contains(".")) {
					parameters.add(new FloatNode(Double.parseDouble(value)));
				}else {
					parameters.add(new IntegerNode(Integer.parseInt(value)));
				}
			}else if((token = this.matchAndRemove(TokenType.TRUE)) != null) {
				parameters.add(new BoolNode(true));
			}else if((token = this.matchAndRemove(TokenType.FALSE)) != null) {
				parameters.add(new BoolNode(false));
			}else if((token = this.matchAndRemove(TokenType.CHARCONTENT)) != null) {
				parameters.add(new CharNode(token.getValue()));
			}else if((token = this.matchAndRemove(TokenType.STRINGCONTENT)) != null) {
				parameters.add(new StringNode(token.getValue()));
			}
			this.matchAndRemove(TokenType.COMMA);
		}
		if(this.matchAndRemove(TokenType.EndOfLine)!= null) {
			return new FunctionCallNode(functionName, parameters);
		}else {
			throw new Exception("must have nothing after the function call statement and start a new line for others");
		}
	}
	
	private AssignmentNode assignments() throws Exception{
		Token varRef = this.matchAndRemove(TokenType.IDENTIFIER);
		VariableReferenceNode variableReferenceNode = new VariableReferenceNode(varRef.getValue());
		// expression method for returning variable expression
		this.matchAndRemove(TokenType.ASSIGNMENT);
		Node expression = expression(false);
		this.matchAndRemove(TokenType.EndOfLine);
		return new AssignmentNode(variableReferenceNode, expression);
	}
	
	private WhileNode whileStatement() throws Exception{
		BooleanExpressionNode booleanExpressionNode = (BooleanExpressionNode)expression(false);
		if(this.matchAndRemove(TokenType.EndOfLine)!= null) {
			return new WhileNode(booleanExpressionNode, bodyFunction());
		}else {
			throw new Exception("must have nothing after the boolean expression and start a new line for body statements");
		}
	}
	
	private ForNode forStatement() throws Exception{
		Token identifier;
		if((identifier = this.matchAndRemove(TokenType.IDENTIFIER)) != null) {
			if(this.matchAndRemove(TokenType.FROM) != null) {
				Token startNumber;
				if((startNumber = this.matchAndRemove(TokenType.NUMBER)) != null) {
					if(this.matchAndRemove(TokenType.TO) != null) {
						Token endNumber;
						if((endNumber = this.matchAndRemove(TokenType.NUMBER)) != null) {
							if(this.matchAndRemove(TokenType.EndOfLine) != null) {
								return new ForNode(new VariableReferenceNode(identifier.getValue()),
										new IntegerNode(Integer.parseInt(startNumber.getValue())), 
										new IntegerNode(Integer.parseInt(endNumber.getValue())), 
										bodyFunction());
							}else {
								throw new Exception("must have nothing after end number and start a new line for body statements");
							}
						}else {
							throw new Exception("must have the end number for variable reference after 'to'");
						}
					}else {
						throw new Exception("must have the 'to' after start number");
					}
				}else {
					throw new Exception("must have the start number for variable reference after 'from'");
				}
			}else {
				throw new Exception("must have the 'from' after variable reference");
			}
		}else {
			throw new Exception("must initialize a variable reference after 'for'");
		}
	}
	
	private IfNode ifStatement() throws Exception{
		BooleanExpressionNode booleanExpression = (BooleanExpressionNode)expression(false);
		ArrayList<StatementNode> statements = new ArrayList<>();
		if(this.matchAndRemove(TokenType.THEN) != null) {
			if(this.matchAndRemove(TokenType.EndOfLine) != null) {
				statements.addAll(bodyFunction());
				if(this.matchAndRemove(TokenType.ELSIF) != null) {
					return new IfNode(booleanExpression, statements, ifStatement());
				}else if(this.matchAndRemove(TokenType.ELSE) != null) {
					if(this.matchAndRemove(TokenType.EndOfLine) != null) {
						return new IfNode(booleanExpression, statements, new IfNode(null, bodyFunction(), null));
					}else {
						throw new Exception("must have nothing after 'else' and start a new line for body statement");
					}
				}else {
					return new IfNode(booleanExpression, statements, null);
				}
			}else {
				throw new Exception("must have nothing after 'then' and start a new line for body statement");
			}
		}else {
			throw new Exception("must have 'then' after boolean expression");
		}
	}
	
	
	private RepeatNode repeatStatement() throws Exception{
		if(this.matchAndRemove(TokenType.EndOfLine) != null) {
			ArrayList<StatementNode> statments = bodyFunction();
			if(this.matchAndRemove(TokenType.UNTIL) != null) {
				BooleanExpressionNode booleanExpression = (BooleanExpressionNode)expression(false);
				if(this.matchAndRemove(TokenType.EndOfLine) != null) {
					return new RepeatNode(booleanExpression, statments);
				}else {
					throw new Exception("must have nothing after the boolean expression");
				}
			}else {
				throw new Exception("must have nothing after 'repeat' and start a new line for body statement");
			}
		}else {
			throw new Exception("must have nothing after");
		}
	}
	
//	private BooleanExpressionNode booleanExpression() throws Exception{
//		Node leftExpression = expression();
//		String condition = "";
//		if(this.matchAndRemove(TokenType.GREATER) != null) {
//			condition = "greater";
//		}else if(this.matchAndRemove(TokenType.LESS) != null) {
//			condition = "less";
//		}else if(this.matchAndRemove(TokenType.GreaterEqual) != null) {
//			condition = "greaterEqual";
//		}else if(this.matchAndRemove(TokenType.LessEqual) != null) {
//			condition = "lessEqual";
//		}else if(this.matchAndRemove(TokenType.EQUAL) != null) {
//			condition = "equal";
//		}else if(this.matchAndRemove(TokenType.NotEqual) != null) {
//			condition = "notEqual";
//		}else {
//			throw new Exception("must add a condition after left expression to evaluate the whole boolean expreesion");
//		}
//		Node rightExpression = expression();
//		return new BooleanExpressionNode(leftExpression, rightExpression, condition);
//	}
	
	private Node expression(boolean booleanCalled) throws Exception {
		Node left = term();

		Token op;
		while((op = this.matchAndRemove(TokenType.PLUS)) != null || (op = this.matchAndRemove(TokenType.MINUS)) != null) {
			if(op.getTokenType() == TokenType.PLUS) {
				left = new MathOpNode('+', left, term());
			}else if(op.getTokenType() == TokenType.MINUS) {
				left = new MathOpNode('-', left, term());
			}
		}
		
		Token bo;
		
		if((bo = this.matchAndRemove(TokenType.GREATER)) != null || (bo = this.matchAndRemove(TokenType.LESS)) != null ||
				(bo = this.matchAndRemove(TokenType.GreaterEqual)) != null || (bo = this.matchAndRemove(TokenType.LessEqual)) != null ||
				(bo = this.matchAndRemove(TokenType.EQUAL)) != null || (bo = this.matchAndRemove(TokenType.NotEqual)) != null) {
			if(!booleanCalled) {
				if( bo.getTokenType() == TokenType.GREATER) {
					left = new BooleanExpressionNode(left, expression(true), "greater");
				}else if( bo.getTokenType() == TokenType.LESS) {
					left = new BooleanExpressionNode(left, expression(true), "less");
				}else if( bo.getTokenType() == TokenType.GreaterEqual) {
					left = new BooleanExpressionNode(left, expression(true), "greaterEqual");
				}else if( bo.getTokenType() == TokenType.LessEqual) {
					left = new BooleanExpressionNode(left, expression(true), "lessEqual");
				}else if( bo.getTokenType() == TokenType.EQUAL) {
					left = new BooleanExpressionNode(left, expression(true), "equal");
				}else if( bo.getTokenType() == TokenType.NotEqual) {
					left = new BooleanExpressionNode(left, expression(true), "notEqual");
				}
			}else {
				throw new Exception("Parser error: can not have chianed boolean");
			}
		}	
		return left;
	}
	
	private Node term() throws Exception {
		Node left = factor();
		
		Token op;
		while((op = this.matchAndRemove(TokenType.TIMES)) != null || (op = this.matchAndRemove(TokenType.DIVIDE)) != null
				|| (op = this.matchAndRemove(TokenType.MOD)) != null) {
			if(op.getTokenType() == TokenType.TIMES) {
				left = new MathOpNode('*', left, factor());
			}else if(op.getTokenType() == TokenType.DIVIDE){
				left = new MathOpNode('/', left, factor());
			}else if(op.getTokenType() == TokenType.MOD){
				left = new MathOpNode('%', left, factor());
			}
		}
		
		return left;
		
	}
	
	private Node factor() throws Exception {
		
		Token token;
		
		// evaluate whether the factor is a number first, sequence for evaluating number or variable doesn't matter
		if((token = this.matchAndRemove(TokenType.NUMBER)) != null) {
			if (token.getValue().contains(".")) {
				return new FloatNode(Double.parseDouble(token.getValue()));
			}else {
				return new IntegerNode(Integer.parseInt(token.getValue()));
			}
		}
		// evaluate whether the factor is a variable
		else if((token = this.matchAndRemove(TokenType.IDENTIFIER)) != null) {
			return new VariableReferenceNode(token.getValue());
		}
		// evaluate whether the factor is true or false
		else if(this.matchAndRemove(TokenType.TRUE) != null) {
			return new BoolNode(true);
		}
		else if(this.matchAndRemove(TokenType.FALSE) != null) {
			return new BoolNode(false);
		}
		// evaluate whether the factor is a character
		else if((token = this.matchAndRemove(TokenType.CHARCONTENT)) != null) {
			return new CharNode(token.getValue());
		}
		// evaluate whether the factor is a string
		else if((token = this.matchAndRemove(TokenType.STRINGCONTENT)) != null) {
			return new StringNode(token.getValue());
		}
		// if neither it may be a nested expression
		else if(this.matchAndRemove(TokenType.LeftParenthesis) != null) {
			
			Node expression = expression(false);
			
			if(expression == null) {
				throw new Exception("Expression should follow after a right parenthesis");
			}
			
			if(this.matchAndRemove(TokenType.RightParenthesis) != null) {
				return expression;
			}else {
				throw new Exception("Right parenthesis should follow after expression");
			}
		}else{
			throw new Exception("Factor should be either number, variable or expression nested in parenthesises");
		}
	}
	
}
