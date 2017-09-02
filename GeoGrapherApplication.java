package geographer;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

public class GeoGrapherApplication {
	
	private static Scanner scanner;
	private static enum Menu {MAIN, FUNC, WINDOW, MANUAL};
	private static GeoGrapherApplication gga;

	public static void main(String[] args) {
		gga = new GeoGrapherApplication();
		scanner = new Scanner(System.in);
		Menu menu = Menu.MAIN;
		String[] main = {"Functions", "Window", "Manual", "Exit"};
		String[] functs = {"Equations", "N-Range", "T-Range", "Back"};
		String[] window = {"Grids", "Circle", "Colors", "Options", "Back"};
		String[] manual = {"Instructions", "Function Catalog", "Back"};
		String menuPrompt = "->:: ";
		
		showTitle();
		
		while(true) {
			System.out.println();
			switch(menu) {
			case MAIN:
				switch(menu("MAIN", main, menuPrompt)) {
				case 1:
					menu = Menu.FUNC; break;
				case 2:
					menu = Menu.WINDOW; break;
				case 3:
					menu = Menu.MANUAL; break;
				case 4:
					scanner.close(); System.exit(0); break;
				}
				break;
				
			case FUNC:
				switch(menu("FUNCTION", functs, menuPrompt)) {
				case 1:
					editFunctions(); break;
				case 2:
					editRangeN(); break;
				case 3:
					editRangeT(); break;
				case 4:
					menu = Menu.MAIN; break;
				}
				break;
				
			case WINDOW:
				switch(menu("WINDOW", window, menuPrompt)) {
				case 1:
					editGridlines(); break;
				case 2:
					editCircle(); break;
				case 3:
					editColors(); break;
				case 4:
					editOptions(); break;
				case 5:
					menu = Menu.MAIN; break;
				}
				break;
				
			case MANUAL:
				switch(menu("MANUAL", manual, menuPrompt)) {
				case 1:
					displayInstructions(); break;
				case 2:
					displayFunctionList(); break;
				case 3:
					menu = Menu.MAIN; break;
				}
				break;
			}
		}
	}
	
	static void showTitle() {
		System.out.println("::::::GeoGrapher::::::");
	}
	
	static int menu(String title, String[] options, String prompt) {
		// display menu
		System.out.println(title);
		int counter = 1;
		for(String s: options) {
			System.out.println(counter + ".)" + s);
			counter++;
		}
			
		int result = 0;
		while(result <= 0 || result > options.length) {
			// get input
			System.out.print(prompt);
			String input = scanner.nextLine();
			
			// interpret input
			if(isInt(input)) {
				result = Integer.parseInt(input);
			} else {
				for(int i = 0; i < options.length; i++) {
					if(input.equalsIgnoreCase(options[i])) {
						result = i+1;
					}
				}
			}
		}
		
		return result;
	}
	
	static boolean isInt(String str) {
		return str.matches("\\d+");
	}
	
	static void editFunctions() {
		boolean hasErrors = true;
		while(hasErrors) {
			// input X equation
			System.out.print("Enter x equation: ");
			gga.setExpression(gga.xexpr, scanner.nextLine());
			
			// input Y equation
			System.out.print("Enter y equation: ");
			gga.setExpression(gga.yexpr, scanner.nextLine());
			
			// display errors
			hasErrors = false;
			if(gga.xexpr.hasErrors()) { // x
				hasErrors = true;
	        	System.out.println("Errors in x equation: ");
	        	showErrors(gga.xexpr);
			}
			if(gga.yexpr.hasErrors()) { // y
				hasErrors = true;
				System.out.println("Errors in y equation: ");
	        	showErrors(gga.yexpr);
			}
		}
		
		gga.displayFunctions();
	}
	
	private static void editRangeN() {
		// nstart
		do {
			// input value
			System.out.print("Enter n start: ");
			gga.setExpression(gga.nstart, scanner.nextLine());
			
			// show errors
			if(gga.nstart.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.nstart);
			}
		} while(gga.nstart.hasErrors());
		
		// nstop
		do {
			// input value
			System.out.print("Enter n stop: ");
			gga.setExpression(gga.nstop, scanner.nextLine());
			
			// show errors
			if(gga.nstop.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.nstop);
			}
		} while(gga.nstop.hasErrors());
		
		// nstep
		do {
			// input value
			System.out.print("Enter n step: ");
			gga.setExpression(gga.nstep, scanner.nextLine());
			
			// show errors
			if(gga.nstep.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.nstep);
			}
		} while(gga.nstep.hasErrors());
		
		gga.displayRangeN();
	}
	
	private static void editRangeT() {
		// tstart
		do {
			// input value
			System.out.print("Enter t start: ");
			gga.setExpression(gga.tstart, scanner.nextLine());
			
			// show errors
			if(gga.tstart.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.tstart);
			}
		} while(gga.tstart.hasErrors());
		
		// tstop
		do {
			// input value
			System.out.print("Enter t stop: ");
			gga.setExpression(gga.tstop, scanner.nextLine());
			
			// show errors
			if(gga.tstop.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.tstop);
			}
		} while(gga.tstop.hasErrors());
		
		// tstep
		do {
			// input value
			System.out.print("Enter t step: ");
			gga.setExpression(gga.tstep, scanner.nextLine());
			
			// show errors
			if(gga.tstep.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.tstep);
			}
		} while(gga.tstep.hasErrors());
		
		gga.displayRangeT();
	}
	
	private static void editGridlines() {
		// xrad
		do {
			// input value
			System.out.print("Enter x range: ");
			gga.setExpression(gga.xrad, scanner.nextLine());
			
			// show errors
			if(gga.xrad.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.xrad);
			}
		} while(gga.xrad.hasErrors());
		
		// yrad
		do {
			// input value
			System.out.print("Enter y range: ");
			gga.setExpression(gga.yrad, scanner.nextLine());
			
			// show errors
			if(gga.yrad.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.yrad);
			}
		} while(gga.yrad.hasErrors());
		
		// xscl
		do {
			// input value
			System.out.print("Enter x scale: ");
			gga.setExpression(gga.xscl, scanner.nextLine());
			
			// show errors
			if(gga.xscl.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.xscl);
			}
		} while(gga.xscl.hasErrors());
		
		// yscl
		do {
			// input value
			System.out.print("Enter y scale: ");
			gga.setExpression(gga.yscl, scanner.nextLine());
			
			// show errors
			if(gga.yscl.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.yscl);
			}
		} while(gga.yscl.hasErrors());
		
		gga.displayGridlines();
	}
	
	private static void editCircle() {
		// circleradius
		do {
			// input value
			System.out.print("Enter circle radius: ");
			gga.setExpression(gga.circleradius, scanner.nextLine());
			
			// show errors
			if(gga.circleradius.hasErrors()) {
				System.out.println("Errors: ");
				showErrors(gga.circleradius);
			}
		} while(gga.circleradius.hasErrors());
		
		gga.displayCircleRadius();
	}
	
	private static void showErrors(Expression expr) {
		Iterator<MathError> it = expr.getErrorIterator();
		while(it.hasNext()) {
    		System.out.println(it.next());
    	}
	}
	
	private static void editColors() {
		String[] text = {"Black", "White", "Gray",
						"Red", "Blue", "Green", 
						"Cyan", "Magenta", "Yellow"};
		
		Color[] colors = {Color.BLACK, Color.WHITE, Color.DARK_GRAY, 
						Color.RED, Color.BLUE, Color.GREEN,
						Color.CYAN, Color.MAGENTA, Color.YELLOW};
		
		// get input
		int bg, grid, circle, shape;
		bg = menu("COLORS", text, "Background Color: ");
		grid = menu("COLORS", text, "Grid Color: ");
		circle = menu("COLORS", text, "Circle Color: ");
		shape = menu("COLORS", text, "Shape Color: ");
		
		// set colors
		gga.gg.setBackgroundColor(colors[bg-1]);
		gga.gg.setGridlineColor(colors[grid-1]);
		gga.gg.setCircleColor(colors[circle-1]);
		gga.gg.setShapeColor(colors[shape-1]);
	}
	
	private static void editOptions() {
		String input;
		boolean isValidInput;
		
		// display grid lines?
		isValidInput = false;
		do {
			System.out.print("Show grids? (y/n): ");
			input = scanner.nextLine();
			if(input.equals("y") || input.equals("1") || input.equals("t")) {
				isValidInput = true;
				gga.gg.setDoGridlines(true);
			} else 
			if(input.equals("n") || input.equals("0") || input.equals("f")) {
				isValidInput = true;
				gga.gg.setDoGridlines(false);
			}
		} while(!isValidInput);
		
		// display circle?
		isValidInput = false;
		do {
			System.out.print("Show circle? (y/n): ");
			input = scanner.nextLine();
			if(input.equals("y") || input.equals("1") || input.equals("t")) {
				isValidInput = true;
				gga.gg.setDoCircle(true);
			} else 
			if(input.equals("n") || input.equals("0") || input.equals("f")) {
				isValidInput = true;
				gga.gg.setDoCircle(false);
			}
		} while(!isValidInput);
		
		// speed
		String[] speeds = {"Fast", "Moderate", "Slow"};
		switch(menu("SPEEDS", speeds, "->:: ")) {
		case 1: // FAST
			gga.gg.setStepLength(10); break;
		case 2: // MODERATE
			gga.gg.setStepLength(200); break;
		case 3: // SLOW
			gga.gg.setStepLength(800); break;
		}
	}
	
	private static void displayInstructions() {
		showFile("instructions.txt");
	}
	
	private static void displayFunctionList() {
		showFile("functions.txt");
	}
	
	private static void showFile(String resourceName) {
		Scanner file = null;
		try {
			URL url = gga.getClass().getResource(resourceName);
			file = new Scanner(new File(url.toURI()));
			while(file.hasNextLine()) {
				System.out.println(file.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.out.println(resourceName + " not found!");
		} catch (URISyntaxException e) {
			System.out.println("Cannot convert URI!");
		} finally {
			try {
				file.close();
			} catch(NullPointerException ex) {}
		}
	}
	
	private Expression xexpr, yexpr; // expressions to graph
	private Expression xrad, yrad, xscl, yscl; // grid line variables
	private Expression nstart, nstop, nstep; // n range variables
	private Expression tstart, tstop, tstep; // t range variables
	private Expression circleradius; // radius of drawn circle
	
	private GeoGrapher gg;
	
	private FunctionList allowed;
	
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
	
	private void setExpression(Expression obj, String expr) {
		obj.setExpression(expr);
		obj.parseExpresison(allowed);
	}
}
