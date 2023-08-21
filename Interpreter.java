package shankCompiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Interpreter {
	
	public static HashMap<String, CallableNode> function = new HashMap<String, CallableNode>();
	
	// for the 'start' function should have no parameter
	public static void interpretFunction(FunctionNode functionNode, ArrayList<InterpreterDataType> parameters) throws Exception {
		// link all parameter with its corresponding name 
		LinkedHashMap<String, InterpreterDataType> parameter = new LinkedHashMap<>();
		
		//  for the user defined function, we will acquire their parameter signatures by function node's getter
		ArrayList<Node> pr = functionNode.getParameters();
		int i = 0;
		HashMap<String, Integer> invocation = new HashMap<String, Integer>();
		for(Node node: pr) {
			// for the 'var' declared parameters
			if(node instanceof VariableReferenceNode) {
				if((((VariableReferenceNode) node).getDataTypeNode() instanceof IntNode) &&
						parameters.get(i) instanceof IntDataType) {
					// add "@" at first to indicate this parameter is a return data type
					// using "@" because it could cause compile error if you add it in your variable name
					parameter.put("@" + ((VariableReferenceNode) node).getName(), parameters.get(i));
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof RealNode) &&
						parameters.get(i) instanceof FloatDataType) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), parameters.get(i));
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof CharNode) &&
						parameters.get(i) instanceof CharDataType) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), parameters.get(i));
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof StringNode) &&
						(parameters.get(i) instanceof StringDataType || parameters.get(i) instanceof CharDataType)) {
					// auto converge character to string
					if(parameters.get(i) instanceof CharDataType) {
						parameter.put("@" + ((VariableReferenceNode) node).getName(), new StringDataType(((CharDataType)parameters.get(i)).getValue()));
					}else {
						parameter.put("@" + ((VariableReferenceNode) node).getName(), parameters.get(i));
					}
					invocation.put("@" + ((VariableReferenceNode)node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof BoolNode) &&
						parameters.get(i) instanceof BoolDataType) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), parameters.get(i));
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				// for the passing by reference variable which defined in the function call, it should point to null
				// so that we assign it to null in hash map and it will get the value by later's inference
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof IntNode) &&
						parameters.get(i)  == null) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), null);
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof RealNode) &&
						parameters.get(i)  == null) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), null);
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof CharNode) &&
						parameters.get(i)  == null) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), null);
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof StringNode) &&
						parameters.get(i)  == null) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), null);
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof BoolNode) &&
						parameters.get(i)  == null) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), null);
					invocation.put("@" + ((VariableReferenceNode) node).getName(), i);
				}
				else {
					throw new Exception("Interpreter error: called function reference variable signature don't match");
				}
				i++;
			}
			// for non-'var' parameters which definitely should already have a value, can't not be none
			else {
				if(((VariableNode) node).getDataTypeNode() instanceof IntNode) {
					parameter.put(((VariableNode) node).getName(), parameters.get(i));
				}else if(((VariableNode) node).getDataTypeNode() instanceof RealNode){
					parameter.put(((VariableNode) node).getName(), parameters.get(i));
				}else if(((VariableNode) node).getDataTypeNode() instanceof CharNode){
					parameter.put(((VariableNode) node).getName(), parameters.get(i));
				}else if(((VariableNode) node).getDataTypeNode() instanceof StringNode){
					parameter.put(((VariableNode) node).getName(), parameters.get(i));
				}else if(((VariableNode) node).getDataTypeNode() instanceof BoolNode){
					parameter.put(((VariableNode) node).getName(), parameters.get(i));
				}else {
					throw new Exception("Interpreter error: called function value variable signature don't match");
				}
				i++;
			}
		}
		// add all within function's variable and constant(with value) into the hash map
		
		// local variables included constants and local defined variables
		LinkedHashMap<String, InterpreterDataType> localVariable = new LinkedHashMap<>();
		ArrayList<Node> lv = new ArrayList<>();
		// since addAll method can't accept null pointer in case that user defined function have no constants or variables
		if(functionNode.getConstants() != null) {
			lv.addAll(functionNode.getConstants());
		}
		if(functionNode.getVariables() != null) {
			lv.addAll(functionNode.getVariables());
		}
		for(Node node: lv) {
			// for constants 
			if(((VariableNode) node).isConstant()) {
				// should assign value to the constant variable
				if(((VariableNode) node).getDataTypeNode() instanceof IntNode) {
					localVariable.put(((VariableNode) node).getName(), new IntDataType(((IntNode)((VariableNode) node).getDataTypeNode()).getValue()));
				}else if (((VariableNode) node).getDataTypeNode() instanceof RealNode) {
					localVariable.put(((VariableNode) node).getName(), new FloatDataType(((RealNode)((VariableNode) node).getDataTypeNode()).getValue()));
				}else if (((VariableNode) node).getDataTypeNode() instanceof CharNode) {
					localVariable.put(((VariableNode) node).getName(), new CharDataType(((CharNode)((VariableNode) node).getDataTypeNode()).getValue()));
				}else if (((VariableNode) node).getDataTypeNode() instanceof StringNode) {
					localVariable.put(((VariableNode) node).getName(), new StringDataType(((StringNode)((VariableNode) node).getDataTypeNode()).getValue()));
				}else if (((VariableNode) node).getDataTypeNode() instanceof BoolNode) {
					localVariable.put(((VariableNode) node).getName(), new BoolDataType(((BoolNode)((VariableNode) node).getDataTypeNode()).getValue()));
				}
			}
			// for local defined variables
			else {
				if(((VariableNode) node).getDataTypeNode() instanceof IntNode) {
					localVariable.put(((VariableNode) node).getName(), new IntDataType());
				}else if(((VariableNode) node).getDataTypeNode() instanceof FloatNode)  {
					localVariable.put(((VariableNode) node).getName(), new FloatDataType());
				}else if(((VariableNode) node).getDataTypeNode() instanceof CharNode)  {
					localVariable.put(((VariableNode) node).getName(), new CharDataType());
				}else if(((VariableNode) node).getDataTypeNode() instanceof StringNode)  {
					localVariable.put(((VariableNode) node).getName(), new StringDataType());
				}else if(((VariableNode) node).getDataTypeNode() instanceof BoolNode)  {
					localVariable.put(((VariableNode) node).getName(), new BoolDataType());
				}
			}
		}
		// combine parameters and local variables together
		LinkedHashMap<String, InterpreterDataType> allVariables = new LinkedHashMap<>();
		allVariables.putAll(parameter);
		allVariables.putAll(localVariable);

		InterpretBlock(functionNode.getStatements(), allVariables);
		
		// after executing the code with begin and end, update the changeable passing by reference variables
		for (Entry<String, Integer> entry : invocation.entrySet()) {
			if(!allVariables.containsKey(entry.getKey())) {
				parameters.set(entry.getValue(), allVariables.get(entry.getKey().substring(1)));
			}else {
				parameters.set(entry.getValue(), allVariables.get(entry.getKey()));
			}
		}
		
	}
	
	public static void InterpretBlock(ArrayList<StatementNode> statements, LinkedHashMap<String, InterpreterDataType> allVariables) throws Exception{
		for(StatementNode statement : statements) {
			// for function call statement 
			if(statement instanceof FunctionCallNode) {
				// create collection of parameters for function call
				ArrayList<InterpreterDataType> interpreterData = new ArrayList<InterpreterDataType>();
				// extract the called function by the calling statement
				// return a FunctionNode or BuildInFunctionNode or null if it not matched
				Node calledFunctionNode = function.get(((FunctionCallNode)statement).getFunctionName());
				// if it matched by name
				if(calledFunctionNode != null) {
					// check whether the called function is user defined or build in 
					// for user define function
					if(calledFunctionNode instanceof FunctionNode) {
						int index = 0;
						HashMap<String, Integer> invocation = new HashMap<String, Integer>();
						// get all function call parameters' data type and value
						ArrayList<Node> callStatement = ((FunctionCallNode)statement).getParameters();
						// get all called function parameters' data type
						ArrayList<Node> func = ((FunctionNode) calledFunctionNode).getParameters();
						// check whether the signature of function call and called function match
						// check parameters amount first 
						if(callStatement.size() == func.size()) {
							// check parameter data type whether match or not 
							for(int i = 0; i< callStatement.size(); i++) {
								// for passing by reference variable and passing by value variable
								if(callStatement.get(i).getClass() == func.get(i).getClass()) {
									// for passing by reference variable
									if(callStatement.get(i) instanceof VariableReferenceNode) {
										String vRName = ((VariableReferenceNode)callStatement.get(i)).getName();
										// for already defined and initialized passing by reference variable
										if(allVariables.containsKey(vRName) || allVariables.containsKey("@" + vRName)) {
											
											if(allVariables.containsKey("@" + vRName)){
												vRName = "@" + vRName;
											}
											// acquire the value of the call function's parameter 
											InterpreterDataType variableReferenceData = allVariables.get(vRName);
											// check the data type whether match or not
											if((variableReferenceData instanceof IntDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof IntNode)) {
												interpreterData.add(variableReferenceData);
												invocation.put(vRName, index);
											}else if((variableReferenceData instanceof FloatDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof RealNode)) {
												interpreterData.add(variableReferenceData);
												invocation.put(vRName, index);
											}
											// auto convert integer to float
											else if((variableReferenceData instanceof IntDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof RealNode)) {
												interpreterData.add(new FloatDataType(((IntDataType)variableReferenceData).getValue()));
												invocation.put(vRName, index);

											}else if((variableReferenceData instanceof CharDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof CharNode)) {
												interpreterData.add(variableReferenceData);
												invocation.put(vRName, index);
											}
											// auto convert char to string
											else if((variableReferenceData instanceof CharDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof StringNode)) {
												interpreterData.add(new StringDataType(((CharDataType)variableReferenceData).getValue()));
												invocation.put(vRName, index);
											}else if((variableReferenceData instanceof StringDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof StringNode)) {
												interpreterData.add(variableReferenceData);
												invocation.put(vRName, index);
											}else if((variableReferenceData instanceof BoolDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof BoolNode)) {
												interpreterData.add(variableReferenceData);
												invocation.put(vRName, index);
											}
											// for declared only no value assigned variable
											else if(variableReferenceData == null) {
												interpreterData.add(variableReferenceData);
												invocation.put(vRName, index);
											}
											else {
												throw new Exception("Interpreter error: function call's pssing by reference variable data type not match");
											}
										}
										// for new defined passing by reference variable which defined in the function call
										// assign the variable value to null at first and value will be assigned later after the function execute
										else {
											allVariables.put("@" + vRName, null);
											interpreterData.add(allVariables.get("@" + vRName));
											invocation.put("@" + vRName, index);
										}										
									}
									// for passing by value variable
									else if(callStatement.get(i) instanceof VariableNode){
										String vRName = ((VariableReferenceNode)callStatement.get(i)).getName();
										// for already defined and initialized variable
										if(allVariables.containsKey(vRName) || allVariables.containsKey("@" + vRName)) {
											if(allVariables.containsKey("@" + vRName)){
												vRName = "@" + vRName;
											}
											
											// acquire the value of the call function's parameter 
											InterpreterDataType variableReferenceData = allVariables.get(vRName);
											// check the data type whether match or not
											if((variableReferenceData instanceof IntDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof IntNode)) {
												interpreterData.add(variableReferenceData);
											}else if((variableReferenceData instanceof FloatDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof RealNode)) {
												interpreterData.add(variableReferenceData);

											}
											// auto convert integer to float
											else if((variableReferenceData instanceof IntDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof RealNode)) {
												interpreterData.add(new FloatDataType(((IntDataType)variableReferenceData).getValue()));

											}else if((variableReferenceData instanceof CharDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof CharNode)) {
												interpreterData.add(variableReferenceData);
											}
											// auto convert char to string
											else if((variableReferenceData instanceof CharDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof StringNode)) {
												interpreterData.add(new StringDataType(((CharDataType)variableReferenceData).getValue()));
											}else if((variableReferenceData instanceof StringDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof StringNode)) {
												interpreterData.add(variableReferenceData);
											}else if((variableReferenceData instanceof BoolDataType) && (((VariableNode)(func.get(i))).getDataTypeNode() instanceof BoolNode)) {
												interpreterData.add(variableReferenceData);
											}
											// for declared only no value assigned variable
											else if(variableReferenceData == null) {
												interpreterData.add(variableReferenceData);
											}
											else {
												throw new Exception("Interpreter error: function call's pssing by value variable data type not match");
											}
										}
										else {
											throw new Exception("Interpreter error: function call's pssing by reference variable data type not match");
										}
										
									}
								}
								// for number, char, string and bool value argument
								else if((callStatement.get(i) instanceof FloatNode || callStatement.get(i) instanceof IntegerNode || 
										callStatement.get(i) instanceof CharNode || callStatement.get(i) instanceof StringNode) && 
										func.get(i) instanceof VariableNode) {
									if(callStatement.get(i) instanceof FloatNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof FloatNode) {
										interpreterData.add(new FloatDataType(((FloatNode)callStatement.get(i)).getFloat()));
									}else if(callStatement.get(i) instanceof IntNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof IntegerNode) {
										interpreterData.add(new IntDataType(((IntNode)callStatement.get(i)).getValue()));
									}else if(callStatement.get(i) instanceof IntNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof FloatNode) {
										interpreterData.add(new IntDataType(((IntNode)callStatement.get(i)).getValue()));
									}else if(callStatement.get(i) instanceof CharNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof StringNode) {
										interpreterData.add(new StringDataType(((CharNode)callStatement.get(i)).getValue()));
									}else if(callStatement.get(i) instanceof CharNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof CharNode) {
										interpreterData.add(new CharDataType(((CharNode)callStatement.get(i)).getValue()));
									}else if(callStatement.get(i) instanceof StringNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof StringNode) {
										interpreterData.add(new StringDataType(((StringNode)callStatement.get(i)).getValue()));
									}
									// always put bool value into variable  
									else {
										throw new Exception("Interpreter error: function call's number value data type not match");
									}
								}
								else {
									throw new Exception("Interpreter error: function call's parameters can't accept");
								}
							}
						}else {
							throw new Exception("Interpreter error: function call's parameters amount not match");
						}
						interpretFunction((FunctionNode)calledFunctionNode, interpreterData);
						
						for (Entry<String, Integer> entry : invocation.entrySet()) {
							if(!allVariables.containsKey(entry.getKey())) {
								allVariables.put(entry.getKey().substring(1), interpreterData.get(entry.getValue()));
							}else {
								allVariables.put(entry.getKey(), interpreterData.get(entry.getValue()));
							}
						}
					}
					// for build in function
					else {
						int index = 0;
						HashMap<String, Integer> invocation = new HashMap<String, Integer>();
						// get call statement's parameters 
						ArrayList<Node> callStatement = ((FunctionCallNode)statement).getParameters();
						for(Node bip: callStatement) {
							// for passing by reference variable
							if(bip instanceof VariableReferenceNode) {
								String variableName = ((VariableReferenceNode)bip).getName();
								// check whether the variable name match the previous initialization
								if(allVariables.containsKey(variableName) || allVariables.containsKey("@" + variableName)) {
									if(allVariables.containsKey("@" + variableName)) {
										variableName = "@" + variableName;
									}
									interpreterData.add(allVariables.get(variableName));
									invocation.put(variableName, index);
								}else {
									variableName = "@" + variableName;
									// for new defined passing by reference variable which defined in the function call
									// assign the variable value to null at first and value will be assigned after the function execute
									allVariables.put(variableName, null);
									// add the null into the list first
									interpreterData.add(allVariables.get(variableName));
									invocation.put(variableName, index);
								}
							}
							// for passing by value variable
							else if(bip instanceof VariableNode) {
								
								String variableName = ((VariableNode)bip).getName();
								// check whether the variable name match the previous initialization
								if(allVariables.containsKey(variableName) || allVariables.containsKey("@" + variableName)) {
									if(allVariables.containsKey("@" + variableName)) {
										variableName = "@" + variableName;
									}
									interpreterData.add(allVariables.get(variableName));
								}else {
									variableName = "@" + variableName;
									// for new defined passing by reference variable which defined in the function call
									// assign the variable value to null at first and value will be assigned after the function execute
									allVariables.put(variableName, null);
									// add the null into the list first
									interpreterData.add(allVariables.get(variableName));
								}
							}
							// for number value argument
							else if((bip instanceof FloatNode) || (bip instanceof IntegerNode)) {
								if(bip instanceof FloatNode) {
									interpreterData.add(new FloatDataType(((FloatNode)bip).getFloat()));
								}else {
									interpreterData.add(new IntDataType(((IntegerNode)bip).getInteger()));
								}
							}
							// for literal value argument
							else if((bip instanceof CharNode) || (bip instanceof StringNode)) {
								if(bip instanceof CharNode) {
									interpreterData.add(new CharDataType(((CharNode)bip).getValue()));
								}else {
									interpreterData.add(new StringDataType(((StringNode)bip).getValue()));
								}
							}else {
								throw new Exception("Interpreter error: function call's parameters can't accept");
							}
							index++;
						}

						if(calledFunctionNode instanceof Read) {
							((Read) calledFunctionNode).execute(interpreterData);
						}else if(calledFunctionNode instanceof Write) {
							((Write) calledFunctionNode).execute(interpreterData);
						}else if(calledFunctionNode instanceof SquareRoot) {
							((SquareRoot) calledFunctionNode).execute(interpreterData);
						}else if(calledFunctionNode instanceof RealToInteger) {
							((RealToInteger) calledFunctionNode).execute(interpreterData);
						}else if(calledFunctionNode instanceof IntegerToReal) {
							((IntegerToReal) calledFunctionNode).execute(interpreterData);
						}else if(calledFunctionNode instanceof GetRandom) {
							((GetRandom) calledFunctionNode).execute(interpreterData);
						}
				
						for (Entry<String, Integer> entry : invocation.entrySet()) {
							if(!allVariables.containsKey(entry.getKey())) {
								allVariables.put(entry.getKey().substring(1), interpreterData.get(entry.getValue()));
							}else {
								allVariables.put(entry.getKey(), interpreterData.get(entry.getValue()));
							}
							
//							System.out.println(entry.getKey());
//							System.out.println(interpreterData.get(entry.getValue()));
						}
					}
					
				}else {
					throw new Exception("Interpreter error: called function name not found");
				}
				
				
				
			}
			else if(statement instanceof AssignmentNode) {
				String leftVName = (((AssignmentNode)statement).getLeft()).getName();
				Node rightExpression = (((AssignmentNode)statement).getRight());
				// check whether the left node is defined or not
				if(allVariables.containsKey(leftVName) || allVariables.containsKey("@" + leftVName)) {
					if(allVariables.containsKey("@" + leftVName)) {
						leftVName = "@" + leftVName;
					}
					// whether left assigned node is initialized which means it has value or not means the value is null
					// the right assignment node's result will assign to the left node 
					if(resolve(rightExpression, allVariables) instanceof Integer) {
						if(allVariables.get(leftVName) instanceof IntDataType) {
							allVariables.put(leftVName, new IntDataType(resolve(rightExpression, allVariables).intValue()));
						}
						// auto convert left int value to a double when the assigned variable is double 
						else {
							allVariables.put(leftVName, new FloatDataType(resolve(rightExpression, allVariables).doubleValue()));
						}
					}else if(resolve(rightExpression, allVariables) instanceof Double) {
						if(allVariables.get(leftVName) instanceof IntDataType) {
							throw new Exception("Interpreter error: Left assigned node is int data type and right expression is float");
						}else {
							allVariables.put(leftVName, new FloatDataType(resolve(rightExpression, allVariables).doubleValue()));
						}
					}else if(resolveLiteral(rightExpression, allVariables) instanceof String) {
						String rightE = resolveLiteral(rightExpression, allVariables);
						if(allVariables.get(leftVName) instanceof CharDataType && rightE.length() == 1) {
							allVariables.put(leftVName, new CharDataType(rightE));
						}else if(allVariables.get(leftVName) instanceof StringDataType){
							allVariables.put(leftVName, new StringDataType(rightE));
						}else {
							throw new Exception("Interpreter error: Left assigned node is char data type and right expression is string");
						}
					}else if(rightExpression instanceof BoolNode) {
						allVariables.put(leftVName, new BoolDataType(((BoolNode)rightExpression).getValue()));
					}
				}else {
					throw new Exception("Interpreter error: Left assigned node initialization not found");
				}
			}
			else if (statement instanceof WhileNode) {
				while(evaluateBooleanExpression(((WhileNode)statement).getBoolean(), allVariables)) {
					InterpretBlock(((WhileNode) statement).getWhileStatements(), allVariables);
				}
			}
			else if (statement instanceof ForNode) {
				int startValue = ((IntegerNode)(((ForNode)statement).getStartNode())).getInteger();
				int endValue = ((IntegerNode)(((ForNode)statement).getEndNode())).getInteger();
				while(startValue <= endValue) {
					allVariables.put((((ForNode)statement).getVariableReferenceNode()).getName(), 
							new IntDataType(startValue));
					InterpretBlock(((ForNode)statement).getForStatements(), allVariables);
					// not sure whether should i allow the variable be used freely
//					allVariables.put((((ForNode)statement).getVariableReferenceNode()).getName(), 
//							new IntDataType(((IntDataType)(allVariables.get((((ForNode)statement).getVariableReferenceNode()).getName()))).getValue() + 1));
//					startValue = ((IntDataType)(allVariables.get((((ForNode)statement).getVariableReferenceNode()).getName()))).getValue() + 1;
					startValue++;
				}
				allVariables.remove((((ForNode)statement).getVariableReferenceNode()).getName());
			}
			else if (statement instanceof IfNode) {
				IfNode ifNode = (IfNode)statement;
				while(!evaluateBooleanExpression(ifNode.getBoolean(), allVariables)) {
					ifNode = ifNode.getNext();
					if(ifNode == null) {
						break;
					}
					continue;
				}
				if(ifNode != null) {
					InterpretBlock(ifNode.getIfstatements(), allVariables);
				}
			}
			else if (statement instanceof RepeatNode) {
				do {
					InterpretBlock(((RepeatNode)statement).getRepeatStatements(), allVariables);
				}while(!evaluateBooleanExpression(((RepeatNode)statement).getBoolean(), allVariables));
			}
		}
	}
	
	public static boolean evaluateBooleanExpression(BooleanExpressionNode booleanNode, LinkedHashMap<String, InterpreterDataType> allVariables) throws Exception {
		Node left = booleanNode.getLeftExpression();
		Node right = booleanNode.getRightExpression();
		
		// for single variable reference for bool
		if((left instanceof VariableReferenceNode && right instanceof VariableReferenceNode) && 
				(allVariables.get(((VariableReferenceNode)left).getName()) instanceof BoolDataType && 
						allVariables.get(((VariableReferenceNode)right).getName()) instanceof BoolDataType)){
			return ((BoolDataType)(allVariables.get(((VariableReferenceNode)right).getName()))).getValue();
		}
		// for literal concatenation
		else if(resolve(left, allVariables) == null || resolve(right, allVariables) == null) {
			String leftValue = resolveLiteral(left, allVariables);
			String rightValue = resolveLiteral(right, allVariables);
			if((booleanNode.getCondtion()).equals("equal")) {
				return leftValue.equals(rightValue);
			}else if((booleanNode.getCondtion()).equals("notEqual")) {
				return !leftValue.equals(rightValue);
			}else {
				throw new Exception("Interpreter error: Literals can only be compared through equal or not equal");
			}
		}else {
			// auto cast both left and right expression value to double since data type is not concerned in here
			double leftValue = resolve(left, allVariables).doubleValue();
			double rightValue = resolve(right, allVariables).doubleValue();	
			if((booleanNode.getCondtion()).equals("greater")) {
				return leftValue > rightValue;
			}else if((booleanNode.getCondtion()).equals("less")) {
				return leftValue < rightValue;
			}else if((booleanNode.getCondtion()).equals("greaterEqual")) {
				return leftValue >= rightValue;
			}else if((booleanNode.getCondtion()).equals("lessEqual")) {
				return leftValue <= rightValue;
			}else if((booleanNode.getCondtion()).equals("equal")) {
				return leftValue == rightValue;
			}else{
				return leftValue != rightValue;
			}
		}
	}
	
	public static String resolveLiteral(Node node, LinkedHashMap<String, InterpreterDataType> allVariables) throws Exception{
		if(node instanceof CharNode) {
			return ((CharNode) node).getValue();
		}else if(node instanceof StringNode) {
			return ((StringNode) node).getValue();
		}else if(node instanceof VariableReferenceNode) {
			if(allVariables.containsKey(((VariableReferenceNode)node).getName())) {			
				InterpreterDataType variableData = allVariables.get(((VariableReferenceNode)node).getName());
	            if (variableData instanceof CharDataType) {
	                return ((CharDataType) variableData).getValue();
	            }else if (variableData instanceof StringDataType) {
	                return ((StringDataType) variableData).getValue();
	            }
			}else {
				throw new Exception("Interpreter error: Left assigned variable reference initialization not found");
			}
		}else if(node instanceof MathOpNode) {
			String left = resolveLiteral(((MathOpNode)node).getLeft(), allVariables);
			String right = resolveLiteral(((MathOpNode)node).getRight(), allVariables);
			String operation = ((MathOpNode) node).getOp().toString();
			if (operation.equals("ADD")) {
				return left + right;
			}else {
				throw new Exception("Interpreter error: Literals can only use add operator");
			}
		}
		return null;
	}
	
	
	
	// return either int or double
	public static <T extends Number> T resolve(Node node, LinkedHashMap<String, InterpreterDataType> allVariables) throws Exception {
		if(node instanceof FloatNode) {			
			return (T) Double.valueOf(((FloatNode)node).getFloat());
		}else if(node instanceof IntegerNode) {
			return (T) Integer.valueOf(((IntegerNode)node).getInteger());
		}else if(node instanceof CharNode || node instanceof StringNode) {
			return null;
		}else if(node instanceof VariableNode) {
			String variableName = ((VariableNode)node).getName();
			if(allVariables.containsKey(variableName) || allVariables.containsKey("@" + variableName)) {	
				if(allVariables.containsKey("@" + variableName)) {
					variableName = "@" + variableName;
				}
				InterpreterDataType variableData = allVariables.get(variableName);
	            if (variableData instanceof IntDataType) {
	                return (T) Integer.valueOf(((IntDataType) variableData).getValue());
	            }else if (variableData instanceof FloatDataType) {
	                return (T) Double.valueOf(((FloatDataType) variableData).getValue());
	            }else if (variableData instanceof CharDataType || variableData instanceof StringDataType) {
	                return null;
	            }
			}else {
				throw new Exception("Interpreter error: Left assigned variable reference initialization not found");
			}
		}else if(node instanceof VariableReferenceNode) {
			String variableName = ((VariableReferenceNode)node).getName();
			if(allVariables.containsKey(variableName) || allVariables.containsKey("@" + variableName)) {
				if(allVariables.containsKey("@" + variableName)) {
					variableName = "@" + variableName;
				}
				InterpreterDataType variableData = allVariables.get(variableName);
	            if (variableData instanceof IntDataType) {
	                return (T) Integer.valueOf(((IntDataType) variableData).getValue());
	            }else if (variableData instanceof FloatDataType) {
	                return (T) Double.valueOf(((FloatDataType) variableData).getValue());
	            }else if (variableData instanceof CharDataType || variableData instanceof StringDataType) {
	                return null;
	            }
			}else {
				throw new Exception("Interpreter error: Left assigned variable reference initialization not found");
			}
		}
		else if(node instanceof MathOpNode){
			T leftValue = resolve(((MathOpNode) node).getLeft(), allVariables);
		    T rightValue = resolve(((MathOpNode) node).getRight(), allVariables);
		    if(leftValue == null || rightValue == null) {
		    	return null;
		    }
		    String operation = ((MathOpNode) node).getOp().toString();
		    if (operation.equals("ADD")) {
	            if(leftValue instanceof Double && rightValue instanceof Integer) {
	                return (T) Double.valueOf(leftValue.doubleValue() + rightValue.intValue());
	            }
	            else if(leftValue instanceof Integer && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.intValue() + rightValue.doubleValue());
	            }
	            else if(leftValue instanceof Double && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.doubleValue() + rightValue.doubleValue());
	            }
	            else{
	                return (T) Integer.valueOf(leftValue.intValue() + rightValue.intValue());
	            }
	        } else if (operation.equals("SUBTRACT")) {
	        	if(leftValue instanceof Double && rightValue instanceof Integer) {
	                return (T) Double.valueOf(leftValue.doubleValue() - rightValue.intValue());
	            }
	            else if(leftValue instanceof Integer && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.intValue() - rightValue.doubleValue());
	            }
	            else if(leftValue instanceof Double && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.doubleValue() - rightValue.doubleValue());
	            }
	            else{
	                return (T) Integer.valueOf(leftValue.intValue() - rightValue.intValue());
	            }
	        } else if (operation.equals("MULTIPLY")) {
	        	if(leftValue instanceof Double && rightValue instanceof Integer) {
	                return (T) Double.valueOf(leftValue.doubleValue() * rightValue.intValue());
	            }
	            else if(leftValue instanceof Integer && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.intValue() * rightValue.doubleValue());
	            }
	            else if(leftValue instanceof Double && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.doubleValue() * rightValue.doubleValue());
	            }
	            else{
	                return (T) Integer.valueOf(leftValue.intValue() * rightValue.intValue());
	            }
	        }else if (operation.equals("DIVIDE")) {
	        	if(leftValue instanceof Double && rightValue instanceof Integer) {
	                return (T) Double.valueOf(leftValue.doubleValue() / rightValue.intValue());
	            }
	            else if(leftValue instanceof Integer && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.intValue() / rightValue.doubleValue());
	            }
	            else if(leftValue instanceof Double && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.doubleValue() / rightValue.doubleValue());
	            }
	            else{
	                return (T) Integer.valueOf(leftValue.intValue() / rightValue.intValue());
	            }
	        }else if (operation.equals("MODULO")) {
	        	if(leftValue instanceof Double && rightValue instanceof Integer) {
	                return (T) Double.valueOf(leftValue.doubleValue() % rightValue.intValue());
	            }
	            else if(leftValue instanceof Integer && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.intValue() % rightValue.doubleValue());
	            }
	            else if(leftValue instanceof Double && rightValue instanceof Double) {
	                return (T) Double.valueOf(leftValue.doubleValue() % rightValue.doubleValue());
	            }
	            else{
	                return (T) Integer.valueOf(leftValue.intValue() % rightValue.intValue());
	            }
	        }
		}
		return null;
	}
}