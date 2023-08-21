package shankCompiler;

import java.util.ArrayList;

public class SemanticAnalysis {

	// check only, no action
	// only check constant variable and passing by value variable 
	// because int and real variable's and passing by reference variable's data type could convert by function
	public static void checkAssignment(ArrayList<FunctionNode> functions) throws Exception {
		for(FunctionNode function: functions) {
			for(StatementNode statement: function.getStatements()) {
				ArrayList<Node> parameters = function.getParameters();
				ArrayList<Node> constants = function.getConstants();
				ArrayList<Node> variables = function.getVariables();
				if(statement instanceof AssignmentNode) {
					VariableReferenceNode leftVR = (((AssignmentNode)statement).getLeft());
					Node rightExpression = (((AssignmentNode)statement).getRight());
					// check whether constant value is tried to change
					if(constants != null) {
						for(Node constant: constants) {
							// if can't not find declaration of the variable name only means no constant variable is tried to change
							// not means absolutely valid assignment, further validation is in interpreter time
							if(((String)leftVR.getName()).equals(((VariableNode)constant).getName())) {
								throw new Exception("Semantic error: should not change the constant variable '" + leftVR.getName() + "'");
							}
						}
					}

					// check whether right hand operation expression valid
					// check only explicit expression 
					if(rightExpression instanceof MathOpNode) {
						rightExpression = checkRightExpression((MathOpNode)rightExpression);
					}
					
					if(variables != null) {
						// check whether right hand value expression valid
						// check whether assigns string value to a character variable or assign non-bool value to bool variable
						// check only literal and bool value cause their variable type can not be changed 
						for(Node variable: variables) {
							if(leftVR.getName().equals( ((VariableNode)variable).getName())) {
								if((((VariableNode)variable).getDataTypeNode() instanceof BoolNode) && !(rightExpression instanceof BoolNode)) { 
									throw new Exception("Semantic error: should not assign bool value to non-bool variable '" + leftVR.getName() + "'");
								}else if((((VariableNode)variable).getDataTypeNode() instanceof StringNode) && 
										!((rightExpression instanceof CharNode) || (rightExpression instanceof StringNode))) {
									throw new Exception("Semantic error: should not assign char or string value to non-string variable '" + leftVR.getName() + "'");
								}else if((((VariableNode)variable).getDataTypeNode() instanceof CharNode) && 
										!(rightExpression instanceof CharNode)) {
									throw new Exception("Semantic error: should not assign char or string value to non-string variable '" + leftVR.getName() + "'");
								}
								// even variable declared as integer first could also be changed by method
								// so only check whether is number or not 
								else if(((((VariableNode)variable).getDataTypeNode() instanceof IntNode) || (((VariableNode)variable).getDataTypeNode() instanceof RealNode)) && 
										!((rightExpression instanceof IntegerNode) || (rightExpression instanceof FloatNode))) {
									throw new Exception("Semantic error: should not assign char or string value to non-string variable '" + leftVR.getName() + "'");
								}
							}
						}
					}
				}
				
				// unfinished if loop check
				// 
				//
				//
				while(statement instanceof IfNode || statement instanceof ForNode || statement instanceof RepeatNode || statement instanceof WhileNode) {
					if(statement instanceof IfNode) {
						ArrayList<FunctionNode> conditionFunction = new ArrayList<FunctionNode>();
						conditionFunction.add(new FunctionNode(function.getFunctionName(), parameters, constants, variables, ((IfNode) statement).getIfstatements()));
						checkAssignment(conditionFunction);
						if(((IfNode) statement).getNext() != null) {
							statement = ((IfNode) statement).getNext();
						}
						break;
					}else if(statement instanceof ForNode) {
						ArrayList<FunctionNode> conditionFunction = new ArrayList<FunctionNode>();
						conditionFunction.add(new FunctionNode(function.getFunctionName(), parameters, constants, variables, ((ForNode) statement).getForStatements()));
						checkAssignment(conditionFunction);
						break;
					}else if(statement instanceof RepeatNode) {
						ArrayList<FunctionNode> conditionFunction = new ArrayList<FunctionNode>();
						conditionFunction.add(new FunctionNode(function.getFunctionName(), parameters, constants, variables, ((RepeatNode) statement).getRepeatStatements()));
						checkAssignment(conditionFunction);
						break;
					}else if(statement instanceof WhileNode) {
						ArrayList<FunctionNode> conditionFunction = new ArrayList<FunctionNode>();
						conditionFunction.add(new FunctionNode(function.getFunctionName(), parameters, constants, variables, ((WhileNode) statement).getWhileStatements()));
						checkAssignment(conditionFunction);
						break;
					}
				}
				
			}
		}
	}
	
	// expression operation should only happens between same types 
	public static Node checkRightExpression(MathOpNode mathOpNode) throws Exception {
		Node left = mathOpNode.getLeft();
		Node right = mathOpNode.getRight();
		ArrayList<Node> nodeInvocation = new ArrayList<Node>();
		
		if(left instanceof MathOpNode) {
			nodeInvocation.add(checkRightExpression((MathOpNode)left)); 
		}
		if(right instanceof MathOpNode) {
			nodeInvocation.add(checkRightExpression((MathOpNode)right)); 
		}
		
		if(left instanceof IntegerNode) {
			nodeInvocation.add(left);
		}else if(left instanceof FloatNode) {
			nodeInvocation.add(left);
		}else if(left instanceof CharNode) {
			nodeInvocation.add(left);
		}else if(left instanceof StringNode) {
			nodeInvocation.add(left);
		}else if(left instanceof BoolNode) {
			throw new Exception("Semantic error: should not use bool value into a math expression");
		}
		
		if(right instanceof IntegerNode) {
			nodeInvocation.add(right);
		}else if(right instanceof FloatNode) {
			nodeInvocation.add(right);
		}else if(right instanceof CharNode) {
			nodeInvocation.add(right);
		}else if(right instanceof StringNode) {
			nodeInvocation.add(right);
		}else if(right instanceof BoolNode) {
			throw new Exception("Semantic error: should not use bool value into a math expression");
		}
		
		int number = 0;
		int literal = 0;
		
		for(Node node : nodeInvocation ) {
			if(node instanceof IntegerNode || node instanceof FloatNode) {
				if(literal == 0) {
					number = 1;
				}else {
					throw new Exception("Semantic error: should not use both literal and number into a math expression");
				}
			}else if(node instanceof CharNode || node instanceof StringNode) {
				if(number == 0) {
					literal = 1;
				}else {
					throw new Exception("Semantic error: should not use both literal and number into a math expression");
				}	
			}
		}
		
		if(number == 1) {
			for(Node node : nodeInvocation) {
				if(node instanceof FloatNode) {
					return new FloatNode(0);
				}else {
					return new IntegerNode(0);
				}
			}
		}else{
			return new StringNode();
		}
		return null;
	}
	
}