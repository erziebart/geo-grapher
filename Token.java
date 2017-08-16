package geographer;

public class Token {
	private String s;
	public enum Type {NUMER, BINOP, NEG, OPENP, CLOSEP, COMMA, LETTER, WHITE};
	private Type type;
	private int pos;
	
	public Token(String s, Type type, int position) {
		this.s = s;
		this.type = type;
		this.pos = position;
	}
	
	@Override
	public String toString() {
		return s;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getPos() {
		return pos;
	}
}
