package lexer;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.regex.Pattern;

import lexer.Token.TokenType;

public class Lexer {

	public ArrayList<Token> lex(String string) throws Exception{
		String state = "s1";
		String numString = "";
		String wordString = "";
		String commentString = "";
		String assignmentString = "";
		String compareString = "";
		
		ArrayList<Token> tokens = new ArrayList<Token>();
		
		HashMap<String, TokenType> reservedWords = new HashMap<>();
		reservedWords.put("define", TokenType.DEFINE);
		reservedWords.put("integer", TokenType.INTEGER);
		reservedWords.put("real", TokenType.REAL);
		reservedWords.put("begin", TokenType.BEGIN);
		reservedWords.put("end", TokenType.END);
		reservedWords.put("variables", TokenType.VARIABLES);
		reservedWords.put("constants", TokenType.CONSTANTS);
		reservedWords.put("if", TokenType.IF);
		reservedWords.put("then", TokenType.THEN);
		reservedWords.put("else", TokenType.ELSE);
		reservedWords.put("elsif", TokenType.ELSIF);
		reservedWords.put("for", TokenType.FOR);
		reservedWords.put("from", TokenType.FROM);
		reservedWords.put("to", TokenType.TO);
		reservedWords.put("while", TokenType.WHILE);
		reservedWords.put("repeat", TokenType.REPEAT);
		reservedWords.put("until", TokenType.UNTIL);
		reservedWords.put("mod", TokenType.MOD);
		reservedWords.put(">", TokenType.GREATER);
		reservedWords.put("<", TokenType.LESS);
		reservedWords.put(">=", TokenType.GreaterEqual);
		reservedWords.put("<=", TokenType.LessEqual);
		reservedWords.put("<>", TokenType.NotEqual);
		reservedWords.put("var", TokenType.VAR);
		
		for(int i = 0; i < string.length(); i++) {
			switch(state) {
			case "s1":
				switch(string.charAt(i)) {
				case ' ':
				case '\t':
					if(numString.matches("\\-?\\d*\\.?\\d+")) {
						if(numString.charAt(0) == '.') {
							tokens.add(new Token(TokenType.NUMBER, "0"+ numString));
						}else if(numString.contains("-.")) {
							tokens.add(new Token(TokenType.NUMBER, "-0"+ numString.substring(1)));
						}else {
							tokens.add(new Token(TokenType.NUMBER, numString));
						}
					}else if(numString.contains("-")) {
						tokens.add(new Token(TokenType.MINUS));
					}else if(numString != "" && !numString.matches("\\-?\\d*\\.?\\d+")) {
						throw new Exception("s1: number input invalid, case SPACE, iteration " + i);
					}else if(wordString.matches("[a-zA-Z][a-zA-Z0-9]*")) {
						if(reservedWords.get(wordString) != null) {
							tokens.add(new Token(reservedWords.get(wordString)));
						}else {
							tokens.add(new Token(TokenType.IDENTIFIER, wordString));
						}
					}else if(commentString.contains("(")) {
						tokens.add(new Token(TokenType.LeftParenthesis));
						commentString = "";
					}else if(wordString != "" && !wordString.matches("[a-zA-Z][a-zA-Z0-9]*")) {
						throw new Exception("s1: letter input invalid, case SPACE, iteration " + i);
					}else if(assignmentString != "") {
						tokens.add(new Token(TokenType.COLON));
						assignmentString = "";
					}else if(compareString != "") {
						if(compareString.contains(">")) {
							tokens.add(new Token(TokenType.GREATER));
						}else if(compareString.contains("<")) {
							tokens.add(new Token(TokenType.LESS));
						}
						compareString = "";
					}
					numString = "";
					wordString = "";
					state = "s1";
					break;
				
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '.':
					if(wordString != "") {
						wordString += string.charAt(i);
						state = "s1";
						break;
					}else if(commentString != "") {
						tokens.add(new Token(TokenType.LeftParenthesis));
						commentString = "";
					}else if(assignmentString != "") {
						tokens.add(new Token(TokenType.COLON));
						assignmentString = "";
					}else if(compareString != "") {
						if(compareString.contains(">")) {
							tokens.add(new Token(TokenType.GREATER));
						}else if(compareString.contains("<")) {
							tokens.add(new Token(TokenType.LESS));
						}
						compareString = "";
					}
					numString += string.charAt(i);
					state = "s1";
					break;			
					
				case '-':
				case '+':
				case '*':
				case '/':
				case '(':
				case ')':
				case ';':
				case ':':
				case '=':
				case ',':
				case '<':
				case '>':
					if(numString.matches("\\-?\\d*\\.?\\d+")) {
						if(numString.charAt(0) == '.') {
							tokens.add(new Token(TokenType.NUMBER, "0"+ numString));
						}else if(numString.contains("-.")) {
							tokens.add(new Token(TokenType.NUMBER, "-0"+ numString.substring(1)));
						}else {
							tokens.add(new Token(TokenType.NUMBER, numString));
						}
						numString = "";
						if(string.charAt(i) == '-') {
							tokens.add(new Token(TokenType.MINUS));
							state = "s1";
							break;
						}
					}else if(numString.length() == 1 && numString.contains("-")) {
						tokens.add(new Token(TokenType.MINUS));
						numString = "";
					}else if(numString != "" && !numString.matches("\\-?\\d*\\.?\\d+")){
						throw new Exception("s1: number input invalid, case OP, iteration " + i);
					}else if(wordString.matches("[a-zA-Z][a-zA-Z0-9]*")) {
						if(reservedWords.get(wordString) != null) {
							tokens.add(new Token(reservedWords.get(wordString)));
						}else {
							tokens.add(new Token(TokenType.IDENTIFIER, wordString));
						}
						wordString = "";
						if(string.charAt(i) == '-') {
							tokens.add(new Token(TokenType.MINUS));
							state = "s1";
							break;
						}
					}else if(wordString != "" && !wordString.matches("[a-zA-Z][a-zA-Z0-9]*")) {
						throw new Exception("s1: letter input invalid, case OP, iteration " + i);
					}else if(commentString.contains("(")) {
						if(string.charAt(i) == '*') {
							commentString = "";
							state = "s2";
							break;
						}
						tokens.add(new Token(TokenType.LeftParenthesis));
						commentString = "";
					}else if(assignmentString.contains(":")) {
						if(string.charAt(i) == '=') {
							assignmentString = "";
							tokens.add(new Token(TokenType.ASSIGNMENT));
							state = "s1";
							break;
						}
						tokens.add(new Token(TokenType.COLON));
						assignmentString = "";
					}else if(compareString != "") {
						if(string.charAt(i) == '=') {
							if(compareString.contains(">")) {
								tokens.add(new Token(TokenType.GreaterEqual));
							}else if(compareString.contains("<")) {
								tokens.add(new Token(TokenType.LessEqual));
							}
							compareString = "";
							state = "s1";
							break;
						}else if(compareString.contains("<") && string.charAt(i) == '>') { 
							tokens.add(new Token(TokenType.NotEqual));
							compareString = "";
							state = "s1";
							break;
						}else {
							if(compareString.contains(">")) {
								tokens.add(new Token(TokenType.GREATER));
							}else if(compareString.contains("<")) {
								tokens.add(new Token(TokenType.LESS));
							}
							compareString = "";
						}
					}
					if(string.charAt(i) == '-') {
						numString += string.charAt(i);
					}else if(string.charAt(i) == '+' ) {
						tokens.add(new Token(TokenType.PLUS));
					}else if(string.charAt(i) == '*' ) {
						tokens.add(new Token(TokenType.TIMES));
					}else if(string.charAt(i) == '/' ) {
						tokens.add(new Token(TokenType.DIVIDE));
					}else if(string.charAt(i) == '(' ) {
						commentString += string.charAt(i);
					}else if(string.charAt(i) == ')' ) {
						tokens.add(new Token(TokenType.RightParenthesis));
					}else if(string.charAt(i) == ';' ) {
						tokens.add(new Token(TokenType.SEMICOLON));
					}else if(string.charAt(i) == ':' ) {
						assignmentString += string.charAt(i);
					}else if(string.charAt(i) == '=' ) {
						tokens.add(new Token(TokenType.EQUAL));
					}else if(string.charAt(i) == ',' ) {
						tokens.add(new Token(TokenType.COMMA));
					}else if(string.charAt(i) == '>' || string.charAt(i) == '<') {
						compareString += string.charAt(i);
					}
					state = "s1";
					break;
					
				default:
					if(numString.matches("\\-?\\d*\\.?\\d+")) {
						if(numString.charAt(0) == '.') {
							tokens.add(new Token(TokenType.NUMBER, "0"+ numString));
						}else if(numString.contains("-.")) {
							tokens.add(new Token(TokenType.NUMBER, "-0"+ numString.substring(1)));
						}else {
							tokens.add(new Token(TokenType.NUMBER, numString));
						}
						numString = "";
					}else if(numString.length() == 1 && numString.contains("-")) {
						tokens.add(new Token(TokenType.MINUS));
						numString = "";
					}else if(numString != "" && !numString.matches("\\-?\\d*\\.?\\d+")){
						throw new Exception("s1: number input invalid, case OP, iteration " + i);
					}else if(commentString.contains("(")) {
						tokens.add(new Token(TokenType.LeftParenthesis));
						commentString = "";
					}else if(assignmentString != "") {
						tokens.add(new Token(TokenType.COLON));
						assignmentString = "";
					}else if(compareString != "") {
						if(compareString.contains(">")) {
							tokens.add(new Token(TokenType.GREATER));
						}else if(compareString.contains("<")) {
							tokens.add(new Token(TokenType.LESS));
						}
						compareString = "";
					}
					wordString += string.charAt(i);
					if (!wordString.matches("[a-zA-Z][a-zA-Z0-9]*")) {
						throw new Exception("s1: letter input invalid, case default, iteration " + i);
					}
					state = "s1";
					break;
				}
				break;
			
			case "s2":
				switch(string.charAt(i)) {
				case '*':
					commentString += string.charAt(i);
					state = "s2";
					break;
				
				case ')':
					if(commentString.contains("*")) {
						commentString = "";
						state = "s1";
						break;
					}else {
						state = "s2";
						break;
					}
				
				default:
					state = "s2";
					break;
				}
				break;
			}
		}
		if(numString.length() == 1 && numString.contains("-")) {
			tokens.add(new Token(TokenType.MINUS));
		}else if(numString != "" && !numString.matches("\\-?\\d*\\.?\\d+")) {
			throw new Exception("s1: end number input invalid");
		}else if(numString != ""){
			if(numString.charAt(0) == '.') {
				tokens.add(new Token(TokenType.NUMBER, "0"+ numString));
			}else if(numString.contains("-.")) {
				tokens.add(new Token(TokenType.NUMBER, "-0"+ numString.substring(1)));
			}else {
				tokens.add(new Token(TokenType.NUMBER, numString));
			}
		}else if(wordString.matches("[a-zA-Z][a-zA-Z0-9]*")) {
			if(reservedWords.get(wordString) != null) {
				tokens.add(new Token(reservedWords.get(wordString)));
			}else {
				tokens.add(new Token(TokenType.IDENTIFIER, wordString));
			}
		}else if(wordString != "" && !wordString.matches("[a-zA-Z][a-zA-Z0-9]*")) {
			throw new Exception("s1: end letter input invalid");
		}else if(commentString != "") { 
			if(commentString.contains("(*")) {
				tokens.add(new Token(TokenType.LeftParenthesis));
				tokens.add(new Token(TokenType.TIMES));
			}else if(commentString.contains("(")) {
				tokens.add(new Token(TokenType.LeftParenthesis));
			}
		}else if(assignmentString != "") {
			if(assignmentString.contains(":")) {
				tokens.add(new Token(TokenType.COLON));
			}
		}else if(compareString != "") {
			if(compareString.contains(">")) {
				tokens.add(new Token(TokenType.GREATER));
			}else if(compareString.contains("<")) {
				tokens.add(new Token(TokenType.LESS));
			}
		}
		tokens.add(new Token(TokenType.EndOfLine));
		return tokens;
	}
}
