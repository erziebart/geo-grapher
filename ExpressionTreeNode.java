package geographer;

import java.util.ArrayList;

public class ExpressionTreeNode {
	boolean f_t, f_n; // flags dependence on variables t,n
	double value;
	private Operation op;
	private ArrayList<ExpressionTreeNode> children;
	
	public ExpressionTreeNode(Operation op) {
		this.f_t = this.f_n = false;
		this.value = 0;
		this.op = op;
		children = new ArrayList<ExpressionTreeNode>();
	}
	
	public void addChild(ExpressionTreeNode node) {
		if(node.f_t) {
			f_t = true;
		}
		if(node.f_n) {
			f_n = true;
		}
		children.add(node);
	}
	
	public int numOperands() {
		if(op != null) {
			return op.argc;
		} else {
			return 0;
		}
	}
	
	public boolean isLeaf() {
		return children.size() == 0;
	}
	
	public boolean isConstant() {
		return !f_t && !f_n;
	}
	
	public boolean isT() {
		return isLeaf() && f_t;
	}
	
	public boolean isN() {
		return isLeaf() && f_n;
	}
	
	public void computeConstants() {
		// base case - do nothing if op is null
		if(op ==  null) {
			return;
		}
		
		// recursive call
		for(ExpressionTreeNode child: children) {
			child.computeConstants();
		}
		
		// compute value
		if(isConstant()) {
			int argc = children.size();
			double[] args = new double[argc];
			
			while(argc > 0) {
				ExpressionTreeNode current = children.get(--argc);
				args[argc] = current.value;
			}
			
			this.value = op.op(args);
		}
	}
	
	public void plugInT(double value) {
		this.value = value; // reset
		
		if(isT()) {
			// plug in value
			this.value = value;
		} else {
			int argc = numOperands();
			double[] args = new double[argc];
			
			// recursive call
			while(argc > 0) {
				ExpressionTreeNode current = children.get(--argc);
				if(current.f_t) {
					current.plugInT(value);
				}
				args[argc] = current.value;
			}
			
			// do op
			try {
				this.value = op.op(args);
			}
			catch(ArrayIndexOutOfBoundsException ex) {
				System.err.print("Wrong number of operands given: " + ex.getMessage());
			}
		}
	}
	
	public void plugInN(double value) {
		this.value = value; // reset
		
		if(isN()) {
			// plug in value
			this.value = value;
		} else {
			int argc = numOperands();
			double[] args = new double[argc];
			
			// recursive call
			while(argc > 0) {
				ExpressionTreeNode current = children.get(--argc);
				if(current.f_n) {
					current.plugInN(value);
				}
				args[argc] = current.value;
			}
			
			// do op
			try {
				this.value = op.op(args);
			}
			catch(ArrayIndexOutOfBoundsException ex) {
				System.err.print("Wrong number of operands given: " + ex.getMessage());
			}
		}
	}
}
