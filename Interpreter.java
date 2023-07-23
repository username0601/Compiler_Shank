package lexer;

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
		ArrayList<Node> pr = new ArrayList<>();
		//  for the user defined function, we will acquire their parameter signatures by function node's getter
		pr.addAll(functionNode.getParameters());
		int i = 0;
		for(Node node: pr) {
			// for the 'var' declared parameters
			if(node instanceof VariableReferenceNode) {
				if((((VariableReferenceNode) node).getDataTypeNode() instanceof IntNode) &&
						parameters.get(i) instanceof IntDataType) {
					// add "@" at first to indicate this parameter is a return data type
					// using "@" because it could cause compile error if you add it in your variable name
					parameter.put("@" + ((VariableReferenceNode) node).getName(), parameters.get(i));
				}
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof RealNode) &&
						parameters.get(i) instanceof FloatDataType) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), parameters.get(i));
				}
				// for the passing by reference variable which defined in the function call, it should point to null
				// we assign it to null in hash map also and get the value by later's inference
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof IntNode) &&
						parameters.get(i)  == null) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), null);
				}
				
				else if ((((VariableReferenceNode) node).getDataTypeNode() instanceof RealNode) &&
						parameters.get(i)  == null) {
					parameter.put("@" + ((VariableReferenceNode) node).getName(), null);
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
				}else {
					localVariable.put(((VariableNode) node).getName(), new FloatDataType(((RealNode)((VariableNode) node).getDataTypeNode()).getValue()));
				}
			}
			// for local defined variables
			else {
				if(((VariableNode) node).getDataTypeNode() instanceof IntNode) {
					localVariable.put(((VariableNode) node).getName(), new IntDataType());
				}else {
					localVariable.put(((VariableNode) node).getName(), new FloatDataType());
				}
			}
		}
		// combine parameters and local variables together
		LinkedHashMap<String, InterpreterDataType> allVariables = new LinkedHashMap<>();
		allVariables.putAll(parameter);
		allVariables.putAll(localVariable);

		InterpretBlock(functionNode.getStatements(), allVariables);
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
										// for already defined and initialized passing by reference variable
										if(allVariables.containsKey("@" + ((VariableReferenceNode)callStatement.get(i)).getName())) {
											// acquire the value of the call function's parameter 
											InterpreterDataType variableReferenceData = allVariables.get("@" + ((VariableReferenceNode)callStatement.get(i)).getName());
											// check the data type whether match or not
											if((variableReferenceData instanceof IntDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof IntNode)) {
												interpreterData.add(variableReferenceData);
											}else if((variableReferenceData instanceof FloatDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof RealNode)) {
												interpreterData.add(variableReferenceData);
											}else {
												throw new Exception("Interpreter error: function call's pssing by reference variable data type not match");
											}
										}
//										if((variableReferenceData = allVariables.get("@" + ((VariableReferenceNode)callStatement.get(i)).getName())) != null) {
//											// check the data type whether match or not
//											if((variableReferenceData instanceof IntDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof IntNode)) {
//												interpreterData.add(variableReferenceData);
//											}else if((variableReferenceData instanceof FloatDataType) && (((VariableReferenceNode)(func.get(i))).getDataTypeNode() instanceof RealNode)) {
//												interpreterData.add(variableReferenceData);
//											}else {
//												throw new Exception("Interpreter error: function call's pssing by reference variable data type not match");
//											}
//										}
										// for new defined passing by reference variable which defined in the function call
										// assign the variable value to null at first and value will be assigned later after the function execute
										else {
											if(((VariableReferenceNode)func.get(i)).getDataTypeNode() instanceof IntNode) {
												allVariables.put("@" + ((VariableReferenceNode)callStatement.get(i)).getName(), null);
												interpreterData.add(allVariables.get("@" + ((VariableReferenceNode)callStatement.get(i)).getName()));
												invocation.put("@" + ((VariableReferenceNode)callStatement.get(i)).getName(), index);
											}else if(((VariableReferenceNode)func.get(i)).getDataTypeNode() instanceof RealNode) {
												allVariables.put("@" + ((VariableReferenceNode)callStatement.get(i)).getName(), null);
												interpreterData.add(allVariables.get("@" + ((VariableReferenceNode)callStatement.get(i)).getName()));
												invocation.put("@" + ((VariableReferenceNode)callStatement.get(i)).getName(), index);
											}
										}										
									}
									// for passing by value variable
									else if(callStatement.get(i) instanceof VariableNode){
										// check if the reference variables reference the same type
										if(((VariableNode)(callStatement.get(i))).getDataTypeNode() == 
												((VariableNode)(func.get(i))).getDataTypeNode()) {
											if(((VariableNode)callStatement.get(i)).getDataTypeNode() instanceof IntNode) {
												// check whether the variable name match the previous initialization
												if(allVariables.containsKey(((VariableNode)callStatement.get(i)).getName())) {
													interpreterData.add(allVariables.get(((VariableNode)callStatement.get(i)).getName()));
												}else {
													throw new Exception("Interpreter error: can't find int variable's initialization");
												}
											}else {
												// check whether the variable name match the previous initialization
												if(allVariables.containsKey(((VariableNode)callStatement.get(i)).getName())) {
													interpreterData.add(allVariables.get(((VariableNode)callStatement.get(i)).getName()));
												}else {
													throw new Exception("Interpreter error: can't find float variable's initialization");
												}
											}
										}else {
											throw new Exception("Interpreter error: function call's value variable data type not match");
										}
									}
								}
								// for number value argument
								else if((callStatement.get(i) instanceof FloatNode || callStatement.get(i) instanceof IntegerNode) && 
										func.get(i) instanceof VariableNode) {
									if(callStatement.get(i) instanceof FloatNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof FloatNode) {
										interpreterData.add(new FloatDataType(((FloatNode)callStatement.get(i)).getFloat()));
									}else if(callStatement.get(i) instanceof IntNode && ((VariableNode)func.get(i)).getDataTypeNode() instanceof IntNode) {
										interpreterData.add(new IntDataType(((IntegerNode)callStatement.get(i)).getInteger()));
									}else {
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
							allVariables.put(entry.getKey(), interpreterData.get(entry.getValue()));
							System.out.println(entry.getKey());
							System.out.println(interpreterData.get(entry.getValue()));
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
								// check whether the variable name match the previous initialization
								if(allVariables.containsKey(((VariableReferenceNode)bip).getName())) {
									interpreterData.add(allVariables.get(((VariableReferenceNode)bip).getName()));
									invocation.put(((VariableReferenceNode)bip).getName(), index);
								}else {
									// for new defined passing by reference variable which defined in the function call
									// assign the variable value to null at first and value will be assigned after the function execute
									allVariables.put("@" + ((VariableReferenceNode)bip).getName(), null);
									// add the null into the list first
									interpreterData.add(allVariables.get("@" + ((VariableReferenceNode)bip).getName()));
									invocation.put("@" + ((VariableReferenceNode)bip).getName(), index);
								}
							}
							// for passing by value variable
							else if(bip instanceof VariableNode) {
								// check whether the variable name match the previous initialization
								if(allVariables.containsKey(((VariableNode)bip).getName())) {
									interpreterData.add(allVariables.get(((VariableNode)bip).getName()));
								}

								else if(allVariables.containsKey(("@" + ((VariableNode)bip).getName()))) {
									interpreterData.add(allVariables.get("@" + ((VariableNode)bip).getName()));
								}
								else {
									System.out.println(bip);
									System.out.println(((VariableNode)bip).getName());
									throw new Exception("Interpreter error: can't find passing by value variable's initialization");
								}
							}
							// for number value argument
							else if((bip instanceof FloatNode) || (bip instanceof IntegerNode)) {
								if(bip instanceof FloatNode) {
									interpreterData.add(new FloatDataType(((FloatNode)bip).getFloat()));
								}else {
									interpreterData.add(new IntDataType(((IntegerNode)bip).getInteger()));
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
							allVariables.put(entry.getKey(), interpreterData.get(entry.getValue()));
							System.out.println(entry.getKey());
							System.out.println(interpreterData.get(entry.getValue()));
						}
					}
					
				}else {
					throw new Exception("Interpreter error: called function name not found");
				}	
			}
			//else if
		}
	}

	public float resolve(Node node) {
		float result = 0;
		
		if(node instanceof FloatNode) {			
			result = (float) ((FloatNode)node).getFloat();
		}else if(node instanceof IntegerNode) {
			result =  (float)((IntegerNode)node).getInteger();
		}else if(node instanceof MathOpNode){
			if(((MathOpNode)node).getOp().toString() == "ADD") {
				result =  resolve(((MathOpNode)node).getLeft()) 
						+ resolve(((MathOpNode)node).getRight()) ;
			}else if(((MathOpNode)node).getOp().toString() == "SUBTRACT") {
				result =  resolve(((MathOpNode)node).getLeft()) 
						- resolve(((MathOpNode)node).getRight()) ;
			}else if(((MathOpNode)node).getOp().toString() == "MULTIPLY") {
				result =  resolve(((MathOpNode)node).getLeft()) 
						* resolve(((MathOpNode)node).getRight()) ;
			}else if(((MathOpNode)node).getOp().toString() == "DIVIDE") {
				result =  resolve(((MathOpNode)node).getLeft()) 
						/ resolve(((MathOpNode)node).getRight()) ;
			}else if(((MathOpNode)node).getOp().toString() == "MODULO") {
				result =  resolve(((MathOpNode)node).getLeft()) 
						% resolve(((MathOpNode)node).getRight()) ;
			}
		}
		return result;
	}
}
