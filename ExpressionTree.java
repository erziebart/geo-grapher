package geographer;

import java.util.EmptyStackException;
import java.util.Stack;

public class ExpressionTree {
	
	private FunctionList using;
	private ExpressionTreeNode head;
	
	public ExpressionTree(String[] tokens, FunctionList using) {
		this.using = using;
		
		Stack<String> operations = new Stack<String>();
		Stack<ExpressionTreeNode> nodes = new Stack<ExpressionTreeNode>();
		
		for(int i = 0; i < tokens.length; i++) {
			String next = tokens[i];
			
			if(isNumeric(next)) { // constant
				ExpressionTreeNode node = createNode(null);
				node.value = Double.parseDouble(next);
				nodes.add(node);
				
			} else if(next.equals("t")) { // variable t
				ExpressionTreeNode node = createNode(null);
				node.f_t = true;
				nodes.add(node);
				
			} else if(next.equals("n")) { // variable n
				ExpressionTreeNode node = createNode(null);
				node.f_n = true;
				nodes.add(node);
				
			} else if(next.equals("(")) { // parentheses
				operations.add(next);
				
			} else if(next.equals(")")) { // close parentheses
				while(!operations.peek().equals("(")) {
					processOperation(operations, nodes);
				}
				operations.pop(); // pop open parentheses
				
			} else if(next.equals(",")) { // comma
				while(!operations.peek().equals("(")) {
					processOperation(operations, nodes);
				}
				
			} else { // normal operation or identifier
				while(!operations.isEmpty() && comparePrecedence(operations.peek(), next) > 0) {
					processOperation(operations, nodes);
				}
				
				if(next.endsWith("(")) { // function
					operations.add(next.substring(0, next.length()-1));
					operations.add("(");
				} else {
					operations.add(next);
				}
			}
		}
		
		// empty the operations stack
		while(!operations.isEmpty()) {
			processOperation(operations, nodes);
		}
		
		// place finished tree
		this.head = nodes.pop();
		
		// check for error
		if(!nodes.isEmpty()) {
			System.err.println("Too Many Operands: " + nodes.size());
		}
	}
	
	// pops the top operation from the operation stack and makes a node
	// using the nodes stack, which is then pushed back onto the node stack
	private void processOperation(Stack<String> operations, Stack<ExpressionTreeNode> nodes) {
		// get the new operation node
		String cur = operations.pop();
		ExpressionTreeNode node = createNode(cur);
		
		// add the children
		if(node != null) {
			int argc = node.numOperands();
			try {
				while(argc-- > 0) {
					node.addChild(nodes.pop());
				}
			}
			catch (EmptyStackException ex) {
				System.err.println("Not Enough Operands: " + ex);
			}
		} else {
			System.err.println("Invalid Operation: " + cur);
		}
		
		// add back to stack
		nodes.add(node);
	}
	
	// returns a new node according to the operation specified
	private ExpressionTreeNode createNode(String operation) {
		Operation op; // operation to perform
		
		if(operation == null) { // Default = number/variable
			op = null;

		} else if(operation.equals("+")) { // Add
			op = new DefOps.Add();
			
		} else if(operation.equals("-")) { // Subtract
			op = new DefOps.Sub();
			
		} else if(operation.equals("*")) { // Multiply
			op = new DefOps.Mult();
			
		} else if(operation.equals("/")) { // Divide
			op = new DefOps.Div();
			
		} else if(operation.equals("~")) { // Negation
			op = new DefOps.Neg();
			
		} else if(operation.equals("^")) { // Exponentiation
			op = new DefOps.Pow();
			
		} else { // check functions
			op = using.get(operation);
			
			if(op == null) { // invalid operation
				return null;
			}
		}
		
		return new ExpressionTreeNode(op);
	}
	
	// isNumeric function
	// regex expression originally found on StackExchange posted by user CraigTP
	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	// returns int indicating relative operator precedence
	// 1 = op1 has higher precedence than op2
	// -1 = op1 has lower precedence than op2
	private static int comparePrecedence(String op1, String op2) {
		if((op1.equals("~") || op1.equals("^"))
		&& (op2.equals("~") || op2.equals("^"))) {
				return -1; // evaluate right to left
		
		} else if (op1.equals(op2)) { // same operation
			return 1; // evaluate left to right
			
		} else if(op1.equals("(")) {
			return -1;
			
		} else if(op2.equals("(")) {
			return 1;
			
		} else { // normal operations
			String[] ops = {"+", "-", "*", "/", "~", "^"};
			int i = 0;
			
			while(i < ops.length) {
				if(op1.equals(ops[i])) {
					return -1;
				}
				if(op2.equals(ops[i])) {
					return 1;
				}
				i++;
			}
			
			return 1; // functions evaluate left to right
		}
	}
	
	public void computeConstants() {
		head.computeConstants();
	}
	
	public void plugInT(double value) {
		if(head.f_t) {
			head.plugInT(value);
		}
	}
	
	public void plugInN(double value) {
		if(head.f_n) {
			head.plugInN(value);
		}
	}
	
	public double getValue() {
		return head.value;
	}
}
