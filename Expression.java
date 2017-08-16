package geographer;

import java.util.ArrayList;
import java.util.Iterator;

public class Expression {
	// expression string
	private String expr;
	
	// tokenized expression
	private ArrayList<Token> tokens;
	
	// errors list
	private ArrayList<MathError> errors;
	
	// functions used
	private FunctionList using;
	
	// constructor
	public Expression() {
		this.expr = null;
		this.tokens = new ArrayList<Token>();
		this.errors = new ArrayList<MathError>();
		this.using = new FunctionList();
	}
	
	// sets the current expression String
	public void setExpression(String expr) {
		this.expr = expr;
	}
	
	// tokenizes and error checks the current expression String
	public void parseExpresison(FunctionList allowedFuncts) {
		reset();
		
		if(expr != null) {
			// catches syntax errors
			tokenizeExpression();
		}
		
		if(errors.isEmpty()) {
			// catches referencing errors
			checkRefs(allowedFuncts);
		}
		
		// printing the tokens
		System.out.print("{ ");
		for(Token t: tokens) {
			System.out.print(t.toString() + " ");
		}
		System.out.print("}");
	}
	
	// resets the token and error lists
	public void reset() {
		// reset errors
		while(!errors.isEmpty()) {
			errors.remove(errors.size()-1);
		}
		
		// reset tokens
		while(!tokens.isEmpty()) {
			tokens.remove(tokens.size()-1);
		}
	}
	
	// return tokenized Strings
	public String[] getTokens() {
		String[] result = new String[tokens.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = tokens.get(i).toString();
		}
		return result;
	}
	
	// return whether the last parsed expression had errors
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	// get the iterator over the syntax errors
	public Iterator<MathError> getErrorIterator() {
		return errors.iterator();
	}
	
	// return the functions used by the expression
	public FunctionList getUsedFunctions() {
		return using;
	}
	
	// splits an expression into smaller token strings
	private void tokenizeExpression() {
		int parentheses = 0; // checking parentheses balance
		boolean operand = true; // operand -or- operator expected
		Token.Type lastType = Token.Type.WHITE;
		int begin, end = 0;
		
		// check if expression is empty
		if(expr.length() == 0) {
			errors.add(new MathError("empty expression",end));
		}
		
		// token creation loop
		while(end < expr.length()) {
			begin = end;
			char c = expr.charAt(end);
			
			if('0' <= c && c <= '9') { // NUMER
				end = scanToEnd(begin, "\\d+(\\.)?(\\d+)?");
				String number = expr.substring(begin, end);
				
				if(operand) {
					// add trailing zero if needed
					if(number.charAt(number.length()-1) == '.') {
						number+="0";
					}
					
					// tokenize number
					lastType = Token.Type.NUMER;
					Token t = new Token(number,lastType,begin);
					tokens.add(t);
					
					// expect an operator next
					operand = false;
					
				} else {
					errors.add(new MathError("operator expected", begin));
				}
				
				
			} else if(c == '+' || c == '*' || c == '/' || c == '^') { // BINOP
				end++;
				if(!operand) {
					// tokenize the character
					lastType = Token.Type.BINOP;
					Token t = new Token(expr.substring(begin, end),lastType,begin);
					tokens.add(t);
					
					// expect an operand next
					operand = true;
					
				} else {
					errors.add(new MathError("operand expected", begin));
				}
				
			} else if(c == '-') { // BINOP -or- NEG
				end++;
				
				if(operand) { // NEG
					// tokenize negation
					lastType = Token.Type.NEG;
					Token t = new Token("~",lastType,begin);
					tokens.add(t);
					/*continue expecting an operand*/
					
				} else { // BINOP
					// tokenize the character
					lastType = Token.Type.BINOP;
					Token t = new Token("-",lastType,begin);
					tokens.add(t);
					
					// expect an operand next
					operand = true;
				}
				
			} else if(c == ',') { // COMMA
				end++; 
				
				if(!operand) {
					// tokenize comma
					lastType = Token.Type.COMMA;
					Token t = new Token(",",lastType,begin);
					tokens.add(t);
					
					// expect an operand next
					operand = true;
				} else {
					errors.add(new MathError("operand expected",begin));
				}
				
			} else if(c == '(') { // OPENP
				if(doImplicitMultiply(lastType)) {
					// tokenize multiply
					Token t = new Token("*",Token.Type.BINOP,begin);
					tokens.add(t);
				}
				
				parentheses+=1;
				
				// tokenize the character
				end++;
				lastType = Token.Type.OPENP;
				Token t = new Token("(",lastType,begin);
				tokens.add(t);
				
				// expect an operand next
				operand = true;
				
			} else if(c == ')') { //CLOSEP
				end++;
				
				parentheses-=1;
				if(parentheses < 0) {
					errors.add(new MathError("parentheses imbalance",begin));
				}
				
				if(!operand) {
					// tokenize the character
					lastType = Token.Type.CLOSEP;
					Token t = new Token(")",lastType,begin);
					tokens.add(t);
					
					/* continue expecting operator */
					
				} else {
					errors.add(new MathError("operand expected",begin));
				}
				
			} else if('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z') { // LETTER
				end = scanToEnd(begin, "[A-Za-z]+\\(?");
				String word = expr.substring(begin, end);
					
				// add implicit multiply if necessary
				if(doImplicitMultiply(lastType)) {
					// tokenize multiply
					Token t = new Token("*",Token.Type.BINOP,begin);
					tokens.add(t);
					
					// expect an operand
					operand = true;
				}
				
				// add the word
				if(operand) {
					// tokenize word
					lastType = Token.Type.LETTER;
					Token t = new Token(word,lastType,begin);
					tokens.add(t);
					
					
					// expect an operator next
					operand = false;
					
					// check for open parentheses
					if(word.endsWith("(")) {
						// tokenize an open parentheses
						lastType = Token.Type.OPENP;
						parentheses++;
						
						// expect an operand next
						operand = true;
					}
					
				} else {
					errors.add(new MathError("operator expected",begin));
				}
				
			} else if(Character.isWhitespace(c)) { // WHITE
				lastType = Token.Type.WHITE;
				end++; // do not tokenize whitespace
				
			} else {
				errors.add(new MathError("unrecognized character "+Character.toString(c),begin));
				lastType = Token.Type.WHITE;
				end++;
			}
		}
		
		// check parentheses balance
		if(parentheses > 0) {
			errors.add(new MathError("parentheses imbalance",end));
		}
		
		// check for valid ending token
		if(!isValidEndToken(lastType)) {
			errors.add(new MathError("cannot end in op or comma",end));
		}
	}
	
	// increases the end point until the regex expression no longer applies 
	private int scanToEnd(int begin, String regex) {
		int end = begin;
		try {
			while(expr.substring(begin, end+1).matches(regex)) {
				end++;
			}
		} catch(StringIndexOutOfBoundsException ex){}
		return end;
	}
	
	private boolean isValidEndToken(Token.Type type) {
		return type != Token.Type.BINOP
				&& type != Token.Type.NEG 
				&& type != Token.Type.COMMA;
	}
	
	private boolean doImplicitMultiply(Token.Type last) {
		return last == Token.Type.NUMER || last == Token.Type.CLOSEP;
	}
	
	private void checkRefs(FunctionList functions) {
		// counting number of arguments passed to functions
		ArrayList<Integer> arguments = new ArrayList<Integer>();
		arguments.add(1);
		int i = 0; // functional depth
		
		for(Token t: tokens) {
			switch (t.getType()) {
			case NUMER:
				arguments.set(i, arguments.get(i)-1);
				
				// check for too any args
				if(arguments.get(i) < 0) {
					errors.add(new MathError("too many arguments",t.getPos()));
				}
				break;
				
			case BINOP:
				arguments.set(i, arguments.get(i)+1);
				break;
				
			case OPENP:
				arguments.set(i, arguments.get(i)-1);
				
				// check for too any args
				if(arguments.get(i) < 0) {
					errors.add(new MathError("too many arguments",t.getPos()));
				}
				
				// increase depth
				i++;
				if(i == arguments.size()) {
					arguments.add(1);
				} else {
					arguments.set(i, 1);
				}
				
				break;
				
			case CLOSEP:
				// check for not enough args
				if(arguments.get(i) > 0) {
					errors.add(new MathError("not enough arguments",t.getPos()));
				}
				
				// decrease depth
				arguments.set(i, 0);
				i--;
				
				break;
				
			case LETTER:
				arguments.set(i, arguments.get(i)-1);
				
				// check for too any args
				if(arguments.get(i) < 0) {
					errors.add(new MathError("too many arguments",t.getPos()));
				}
				
				String name;
				if(t.toString().endsWith("(")) { // FUNCTION
					name = t.toString().substring(0, t.toString().length()-1);
					
					// search for function with name
					Function f;
					if((f = functions.get(name)) != null) {
						// add to list
						using.add(f);
						
						// increase depth
						i++;
						if(i == arguments.size()) {
							arguments.add(f.argc);
						} else {
							arguments.set(i, f.argc);
						}
						
					} else {
						errors.add(new MathError("unknown function " + name,t.getPos()));
						
						// increase depth
						i++;
						if(i == arguments.size()) {
							arguments.add(1);
						} else {
							arguments.set(i, 1);
						}
					}
					
					
					
				} else { // CONSTANT -or- VARIABLE
					name = t.toString();
					
					// check for n and t variables
					if(name.compareTo("n") == 0 || name.compareTo("t") == 0) {
						break;
					}
					
					// search for constant with name
					Function f;
					if((f = functions.get(name)) != null && f instanceof Constant) {
						// add to list
						using.add(f);
						
					} else {
						errors.add(new MathError("unknown constant " + name,t.getPos()));
					}
				}
				
				break;
				
			default:
				break;
			}	
		}
		
		// check for not enough args
		if(arguments.get(0) > 0) {
			errors.add(new MathError("not enough arguments",expr.length()));
		}
	}
}
