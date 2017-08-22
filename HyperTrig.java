package geographer;

public class HyperTrig {
	static class HyperSine extends Function {
		public HyperSine(){super("sinh",1);}
		@Override
		public double op(double[] operands) {
			return Math.sinh(operands[0]);
		}
	}
	
	static class HyperCosine extends Function {
		public HyperCosine(){super("cosh",1);}
		@Override
		public double op(double[] operands) {
			return Math.cosh(operands[0]);
		}
	}
	
	static class HyperTangent extends Function {
		public HyperTangent(){super("tanh",1);}
		@Override
		public double op(double[] operands) {
			return Math.tanh(operands[0]);
		}
	}
	
	static class HyperCosecant extends Function {
		public HyperCosecant(){super("csch",1);}
		@Override
		public double op(double[] operands) {
			return 1/Math.sinh(operands[0]);
		}
	}
	
	static class HyperSecant extends Function {
		public HyperSecant(){super("sech",1);}
		@Override
		public double op(double[] operands) {
			return 1/Math.cosh(operands[0]);
		}
	}
	
	static class HyperCotangent extends Function {
		public HyperCotangent(){super("coth",1);}
		@Override
		public double op(double[] operands) {
			return 1/Math.tanh(operands[0]);
		}
	}
	
	static class HyperSineInverse extends Function {
		public HyperSineInverse(){super("asinh",1);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			return Math.log(x+Math.sqrt(x*x+1));
		}
	}
	
	static class HyperCosineInverse extends Function {
		public HyperCosineInverse(){super("acosh",1);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			return Math.log(x+Math.sqrt(x*x-1));
		}
	}
	
	static class HyperTangentInverse extends Function {
		public HyperTangentInverse(){super("atanh",1);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			return 0.5*Math.log((1+x)/(1-x));
		}
	}
	
	static class HyperCotangentInverse extends Function {
		public HyperCotangentInverse(){super("acoth",1);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			return 0.5*Math.log((x+1)/(x-1));
		}
	}
	
	static class HyperSecantInverse extends Function {
		public HyperSecantInverse(){super("asech",1);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			return Math.log((1+Math.sqrt(1-x*x))/(x));
		}
	}
	
	static class HyperCosecantInverse extends Function {
		public HyperCosecantInverse(){super("acsch",1);}
		@Override
		public double op(double[] operands) {
			double x = operands[0];
			return Math.log((1+Math.sqrt(1+x*x))/(x));
		}
	}
}
