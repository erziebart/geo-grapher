package geographer;

public class Functs {
	static class NatLog extends Function {
		public NatLog() {super("ln", 1);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			return (x < 0.1) ? Math.log(x) : Math.log1p(x-1.0);
		}
	}
		
	static class Log extends Function {
		public Log() {super("log", 2);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			double a = operands[1];
			return ((x < 0.1) ? Math.log(x) : Math.log1p(x-1.0)) /
					((a < 0.1) ? Math.log(a) : Math.log1p(a-1.0));
		}
	}
	
	static class Sqrt extends Function {
		public Sqrt() {super("sqrt",1);}
		@Override
		public double op(double[] operands) {
			return Math.sqrt(operands[0]);
		}
	}
	
	static class Exp extends Function {
		public Exp() {super("exp",1);}
		@Override
		public double op(double[] operands) {
			return Math.exp(operands[0]);
		}
	}
	
	static class E extends Constant {
		public E() {super("e");}
		@Override
		public double op(double[] operands) {
			return Math.E;
		}
	}
}
