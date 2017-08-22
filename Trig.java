package geographer;

public class Trig {
	static class Sine extends Function {
		public Sine() {super("sin", 1);}
		@Override
		public double op(double[] operands) {
			return Math.sin(operands[0]);
		}
	}
	
	static class Cosine extends Function {
		public Cosine() {super("cos", 1);}
		@Override
		public double op(double[] operands) {
			return Math.cos(operands[0]);
		}
	}
	
	static class Tangent extends Function {
		public Tangent() {super("tan", 1);}
		@Override
		public double op(double[] operands) {
			return Math.tan(operands[0]);
		}
	}
	
	static class Cotangent extends Function {
		public Cotangent() {super("cot", 1);}
		@Override
		public double op(double[] operands) {
			return 1/Math.tan(operands[0]);
		}
	}
	
	static class Secant extends Function {
		public Secant() {super("sec", 1);}
		@Override
		public double op(double[] operands) {
			return 1/Math.cos(operands[0]);
		}
	}
	
	static class Cosecant extends Function {
		public Cosecant() {super("csc", 1);}
		@Override
		public double op(double[] operands) {
			return 1/Math.sin(operands[0]);
		}
	}
	
	static class Pi extends Constant {
		public Pi(){super("pi");}
		@Override
		public double op(double[] operands) {
			return Math.PI;
		}
	}
	
	static class Tau extends Constant {
		public Tau(){super("tau");}
		@Override
		public double op(double[] operands) {
			return 2*Math.PI;
		}
	}
	
	static class Arcsine extends Function {
		public Arcsine(){super("asin",1);}
		@Override
		public double op(double[] operands) {
			return Math.asin(operands[0]);
		}
	}
	
	static class Arccosine extends Function {
		public Arccosine(){super("acos",1);}
		@Override
		public double op(double[] operands) {
			return Math.acos(operands[0]);
		}
	}
	
	static class Arctangent extends Function {
		public Arctangent(){super("atan",1);}
		@Override
		public double op(double[] operands) {
			return Math.atan(operands[0]);
		}
	}
	
	static class Arcsecant extends Function {
		public Arcsecant(){super("asec",1);}
		@Override
		public double op(double[] operands) {
			return Math.acos(1/operands[0]);
		}
	}
	
	static class Arccosecant extends Function {
		public Arccosecant(){super("acsc",1);}
		@Override
		public double op(double[] operands) {
			return Math.asin(1/operands[0]);
		}
	}
	
	static class Arccotangent extends Function {
		public Arccotangent(){super("acot",1);}
		@Override
		public double op(double[] operands) {
			return 0.5*Math.PI - Math.atan(operands[0]);
		}
	}
	
	static class ToDegree extends Function {
		public ToDegree(){super("deg",1);}
		@Override
		public double op(double[] operands) {
			return operands[0]*180/Math.PI;
		}
	}
	
	static class ToRadian extends Function {
		public ToRadian(){super("rad",1);}
		@Override
		public double op(double[] operands) {
			return operands[0]/180*Math.PI;
		}
	}
}
