package geographer;

import java.util.ArrayList;
import java.util.Iterator;

public class Tokenizer {
	enum TokenType {NUMER, BINOP, NEG, OPENP, CLOSEP, COMMA, LETTER, WHITE};
	private ArrayList<MathSyntaxError> errors = new ArrayList<MathSyntaxError>();
	
	private FunctionList functions;
	private FunctionList using;
	
	public Tokenizer() {
		this.functions = new FunctionList();
		this.functions.loadFunctionList();
		this.using = new FunctionList();
	}
	
	public String[] tokenizeExpression(String expr) {
		ArrayList<String> arr = new ArrayList<String>();
		int parentheses = 0; // checking parentheses balance
		boolean operand = true; // operand -or- operator expected
		//int fargs = 0; // counting arguments passed to functions
		TokenType lastType = TokenType.WHITE;
		int begin, end = 0;
		
		// check if expression is empty
		if(expr.length() == 0) {
			errors.add(new MathSyntaxError("empty expression",end));
		}
		
		// token creation loop
		while(end < expr.length()) {
			begin = end;
			char c = expr.charAt(end);
			
			if('0' <= c && c <= '9') { // NUMER
				// scan to end of number
				try {
					while(expr.substring(begin, end+1).matches("\\d+(\\.)?(\\d+)?")) {
						end++;
					}
				} catch(StringIndexOutOfBoundsException ex) {}
				String number = expr.substring(begin, end);
				
				if(operand) {
					// add trailing zero if needed
					if(number.charAt(number.length()-1) == '.') {
						number+="0";
					}
					
					// tokenize number
					arr.add(number);
					lastType = TokenType.NUMER;
					
					// expect an operator next
					operand = false;
					
				} else {
					errors.add(new MathSyntaxError("operator expected", begin));
				}
				
				
			} else if(c == '+' || c == '*' || c == '/' || c == '^') { // BINOP
				end++;
				if(!operand) {
					// tokenize the character
					arr.add(expr.substring(begin, end));
					lastType = TokenType.BINOP;
					
					// expect an operand next
					operand = true;
					
				} else {
					errors.add(new MathSyntaxError("operand expected", begin));
				}
				
			} else if(c == '-') { // BINOP -or- NEG
				end++;
				
				if(operand) { // NEG
					// tokenize negation
					arr.add("~");
					lastType = TokenType.NEG;
					/*continue expecting an operand*/
					
				} else { // BINOP
					// tokenize the character
					arr.add("-");
					lastType = TokenType.BINOP;
					
					// expect an operand next
					operand = true;
				}
				
			} else if(c == ',') { // COMMA
				end++; // do not tokenize comma
				
				if(!operand) {
					// set lastType
					lastType = TokenType.COMMA;
					
					// expect an operand next
					operand = true;
				} else {
					errors.add(new MathSyntaxError("operand expected",begin));
				}
				
			} else if(c == '(') { // OPENP
				if(doImplicitMultiply(lastType)) {
					// tokenize multiply
					arr.add("*");
					lastType = TokenType.BINOP;
				}
				
				parentheses+=1;
				
				// tokenize the character
				end++;
				arr.add("(");
				lastType = TokenType.OPENP;
				
				// expect an operand next
				operand = true;
				
			} else if(c == ')') { //CLOSEP
				end++;
				
				parentheses-=1;
				if(parentheses < 0) {
					errors.add(new MathSyntaxError("parentheses imbalance",begin));
				}
				
				if(!operand) {
					// tokenize the character
					arr.add(")");
					lastType = TokenType.CLOSEP;
					/* continue expecting operator */
					
				} else {
					errors.add(new MathSyntaxError("operand expected",begin));
				}
				
			} else if('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z') { // LETTER
				// scan to end of word
				try {
					while(expr.substring(begin, end+1).matches("\\w+")) {
						end++;
					}
				} catch(StringIndexOutOfBoundsException ex) {}
				String word = expr.substring(begin, end);
				
				Function f = functions.get(word);
				if(f != null) {
					// add to list of used functions
					using.add(f);
				}
				
				// check if valid function
				if(word.equals("t") || word.equals("n") 
						|| f != null) {
					
					// add implicit multiply if necessary
					if(doImplicitMultiply(lastType)) {
						// tokenize multiply
						arr.add("*");
						
						// expect an operand
						operand = true;
					}
					
					// add the word
					if(operand) {
						// tokenize word
						arr.add(word);
						lastType = TokenType.LETTER;
						
						// expect an operator next
						operand = false;
						
					} else {
						errors.add(new MathSyntaxError("operator expected",begin));
					}
					
				} else {
					errors.add(new MathSyntaxError("unknown name", begin));
				}
				
			} else if(Character.isWhitespace(c)) {
				lastType = TokenType.WHITE;
				end++; // do not tokenize whitespace
				
			} else {
				errors.add(new MathSyntaxError("unrecognized character "+Character.toString(c),begin));
				end++;
			}
		}
		
		// check parentheses balance
		if(parentheses > 0) {
			errors.add(new MathSyntaxError("parentheses imbalance",end));
		}
		
		// check for valid ending token
		if(!isValidEndToken(lastType)) {
			errors.add(new MathSyntaxError("cannot end in op or comma",end));
		}
		
		String[] result = new String[arr.size()];
		return arr.toArray((String[]) result);
	}
	
	private boolean isValidEndToken(TokenType type) {
		return type != TokenType.BINOP
				&& type != TokenType.NEG 
				&& type != TokenType.COMMA;
	}
	
	private boolean doImplicitMultiply(TokenType last) {
		return last == TokenType.NUMER || last == TokenType.CLOSEP;
	}
	
	public void resetErrors() {
		while(!errors.isEmpty()) {
			errors.remove(errors.size()-1);
		}
	}
	
	public Iterator<MathSyntaxError> getErrorIterator() {
		return errors.iterator();
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public FunctionList getUsedFunctions() {
		return using;
	}
}
