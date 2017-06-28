package geographer;

public class DefOps {
	static class Add extends Operation {
		Add(){super(2);}
		@Override
		public double op(double[] operands) {
			return operands[1]+operands[0];
		}
	}
	
	static class Sub extends Operation {
		Sub(){super(2);}
		@Override
		public double op(double[] operands) {
			return operands[1]-operands[0];
		}
	}
	
	static class Mult extends Operation {
		Mult(){super(2);}
		@Override
		public double op(double[] operands) {
			return operands[1]*operands[0];
		}
	}
	
	static class Div extends Operation {
		Div(){super(2);}
		@Override
		public double op(double[] operands) {
			return operands[1]/operands[0];
		}
	}
	
	static class Neg extends Operation {
		Neg(){super(1);}
		@Override
		public double op(double[] operands) {
			return -operands[0];
		}
	}
	
	static class Exp extends Operation {
		Exp(){super(2);}
		@Override
		public double op(double[] operands) {
			return Math.pow(operands[1],operands[0]);
		}
	}
}
