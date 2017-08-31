package geographer;

import java.util.Iterator;

public class GeoGrapherApplication {
	
	Expression xexpr, yexpr; // expressions to graph
	Expression xrad, yrad, xscl, yscl; // grid line variables
	Expression nstart, nstop, nstep; // n range variables
	Expression tstart, tstop, tstep; // t range variables
	Expression circleradius; // radius of drawn circle
	
	GeoGrapher gg;
	
	FunctionList allowed;

	public static void main(String[] args) {
		GeoGrapherApplication gga = new GeoGrapherApplication();
		
		gga.setExpression(gga.xexpr, "n");
		gga.setExpression(gga.yexpr, "sin(n+tau*t)");
		
		gga.setExpression(gga.xrad, "3tau");
		gga.setExpression(gga.yrad, "2");
		gga.setExpression(gga.xscl, "tau/4");
		gga.setExpression(gga.yscl, "1");
		
		gga.setExpression(gga.tstart,"1");
		gga.setExpression(gga.tstop, "10");
		gga.setExpression(gga.tstep, "1/2^7");
		
		gga.setExpression(gga.nstart, "-3tau");
		gga.setExpression(gga.nstop, "3tau");
		gga.setExpression(gga.nstep, "tau/360");
		
		gga.displayGridlines();
		gga.displayRangeN();
		gga.displayRangeT();
		gga.displayFunctions();
	}
	
	public GeoGrapherApplication() {
		// functions/shapes
		xexpr = new Expression(true);
		xexpr.setExpression("n");
		yexpr = new Expression(true);
		yexpr.setExpression("n");
		
		// gridlines
		xrad = new Expression(false);
		xrad.setExpression("15");
		yrad = new Expression(false);
		yrad.setExpression("15");
		xscl = new Expression(false);
		xscl.setExpression("1");
		yscl = new Expression(false);
		yscl.setExpression("1");
		
		// n
		nstart = new Expression(false);
		nstart.setExpression("-15");
		nstop = new Expression(false);
		nstop.setExpression("15");
		nstep = new Expression(false);
		nstep.setExpression("0.015625");
		
		// t
		tstart = new Expression(false);
		tstart.setExpression("-15");
		tstop = new Expression(false);
		tstop.setExpression("15");
		tstep = new Expression(false);
		tstep.setExpression("0.125");
		
		// circle
		circleradius = new Expression(false);
		circleradius.setExpression("10");
		
		// function list
		allowed = new FunctionList();
		allowed.loadFunctionList();
		
		// grapher window
		gg = new GeoGrapher(1500, 800);
		gg.setVisible(true);
	}
	
	private void displayFunctions() {
		gg.setFunctions(xexpr.getTokens(), yexpr.getTokens(), 
						xexpr.getUsedFunctions(), yexpr.getUsedFunctions());
	}
	
	private void displayGridlines() {
		FunctionList using = new FunctionList();
		using.addAll(xrad.getUsedFunctions());
		using.addAll(yrad.getUsedFunctions());
		using.addAll(xscl.getUsedFunctions());
		using.addAll(yscl.getUsedFunctions());
		
		gg.setGridlines(xrad.getTokens(), yrad.getTokens(), 
				xscl.getTokens(), yscl.getTokens(), using);
	}
	
	private void displayRangeN() {
		FunctionList using = new FunctionList();
		using.addAll(nstart.getUsedFunctions());
		using.addAll(nstop.getUsedFunctions());
		using.addAll(nstep.getUsedFunctions());
		
		gg.setRangeN(nstart.getTokens(), nstop.getTokens(),
					nstep.getTokens(), using);
	}
	
	private void displayRangeT() {
		FunctionList using = new FunctionList();
		using.addAll(tstart.getUsedFunctions());
		using.addAll(tstop.getUsedFunctions());
		using.addAll(tstep.getUsedFunctions());
		
		gg.setRangeT(tstart.getTokens(), tstop.getTokens(), 
					tstep.getTokens(), using);
	}
	
	private void displayCircleRadius() {
		gg.setCircleRadius(circleradius.getTokens(), 
						circleradius.getUsedFunctions());
	}
	
	private Iterator<MathError> setExpression(Expression obj, String expr) {
		obj.setExpression(expr);
		obj.parseExpresison(allowed);
		
		if(obj.hasErrors()) {
			return obj.getErrorIterator();
		} else {
			return null;
		}
	}
}
