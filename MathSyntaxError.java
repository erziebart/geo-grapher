package geographer;

public class MathSyntaxError {
	String what;
	int position;
	
	public MathSyntaxError(String what, int position) {
		this.what = what;
		this.position = position;
	}
	
	@Override
	public String toString() {
		return (what + " at " + Integer.toString(position));
	}
}
