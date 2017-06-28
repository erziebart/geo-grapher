package geographer;

public abstract class Operation {
	int argc;
	
	public Operation(int argc) {
		this.argc = argc;
	}
	
	public abstract double op(double[] operands) 
			throws ArrayIndexOutOfBoundsException;
}
