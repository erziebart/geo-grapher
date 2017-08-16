package geographer;

public class MathError {
	String what;
	int position;
	
	public MathError(String what, int position) {
		this.what = what;
		this.position = position;
	}
	
	@Override
	public String toString() {
		return (what + " at " + Integer.toString(position));
	}
}
