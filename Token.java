package lexer;

public class Token {
	
	public enum TokenType {EndOfLine, NUMBER, PLUS, MINUS, TIMES, DIVIDE, LeftParenthesis, 
		RightParenthesis, IDENTIFIER, DEFINE, INTEGER, REAL, BEGIN, END, SEMICOLON, COLON,
		EQUAL, COMMA, VARIABLES, CONSTANTS, ASSIGNMENT, IF, THEN, ELSE, ELSIF, FOR, FROM,
		TO, WHILE, REPEAT, UNTIL, MOD, GREATER, LESS, GreaterEqual, LessEqual, NotEqual, VAR}
	
	private TokenType tokenType;
	private String value;
	
	Token(TokenType tokenType){
		this.tokenType = tokenType;
		this.value = null;
	}
	
	Token(TokenType tokenType, String value){
		this.tokenType = tokenType;
		this.value = value;
	}
	
	public TokenType getTokenType() {
		return this.tokenType;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toString(){
//		return this.token.toString() + value != null ? "(" + value + ")" : "";
		if (this.tokenType == TokenType.NUMBER){
			return this.tokenType.toString() + "(" + this.value + ")";
		}else if (this.tokenType == TokenType.IDENTIFIER){
			return this.tokenType.toString() + "(" + this.value + ")";
		}else {
			return this.tokenType.toString();
		}

		
	}

	
}
