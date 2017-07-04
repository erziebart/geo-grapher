package geographer;

public abstract class Function extends Operation {
	String name;
	
	public Function(String name, int argc) {
		super(argc);
		this.name = name;
	}
}
