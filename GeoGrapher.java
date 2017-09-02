package geographer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class GeoGrapher extends JFrame{
    
	private static final long serialVersionUID = 1L;

	private double xradius, yradius;
    private double xscale, yscale;
    private int originx, originy;
    private float xpixels, ypixels;
    private double circleradius;
    
    private double n;
    private double nstart, nstop, nstep;
    
    private double time;
    private double tstart, tstop, tstep;
    private long steplength;
    
    private boolean doCircle;
    private boolean doGrids;
    
    private Color bg;
    private Color circle;
    private Color grid;
    private Color shape;
    
    private ExpressionTree xfunction, yfunction; // parametric functions

    public static void main(String[] args) {
    	// the expressions
    	Expression exprX, exprY;
    	exprX = new Expression(true);
    	exprY = new Expression(true);
    	
    	try {
    		exprX.setExpression(args[0]);
    		exprY.setExpression(args[1]);
    	} catch(ArrayIndexOutOfBoundsException ex1) {
    		try {
    			exprX.setExpression("n");
    			exprY.setExpression(args[0]);
    		} catch(ArrayIndexOutOfBoundsException ex2) {
    			System.out.println("Usage: java GeoGrapher [exprX] [exprY]");
        		System.out.println("Usage: java GeoGrapher [exprY]");
        		
        		// default expressions -- for debug
                exprX.setExpression("n");
                exprY.setExpression("n");
    		}
    	}
    	
    	// get default function list
    	FunctionList defaults = new FunctionList();
    	defaults.loadFunctionList();
    	
    	// parse expressions
    	exprX.parseExpresison(defaults);
    	exprY.parseExpresison(defaults);
    	
    	// check for errors
    	boolean hasErrors = false;
    	if(exprX.hasErrors()) {
    		hasErrors = true;
        	System.out.println("Errors in X equation: ");
        	Iterator<MathError> it = exprX.getErrorIterator();
        	while(it.hasNext()) {
        		System.out.println(it.next());
        	}
    	}
    	if(exprY.hasErrors()) {
    		hasErrors = true;
        	System.out.println("Errors in Y equation: ");
        	Iterator<MathError> it = exprY.getErrorIterator();
        	while(it.hasNext()) {
        		System.out.println(it.next());
        	}
    	}
    	
    	if(!hasErrors) {
        	// make grapher
	        GeoGrapher gg = new GeoGrapher(1500,800);
	        gg.setFunctions(exprX.getTokens(), exprY.getTokens(), 
	        		exprX.getUsedFunctions(), exprY.getUsedFunctions());
	        gg.setVisible(true);
        }
    }
    
    public GeoGrapher(int width, int height) {
        xradius = 15;
        yradius = 15;
        xscale = 1;
        yscale = 1;
        circleradius = 10;
        
        originx = width/2;
        originy = height/2;
        
        xpixels = originx / (float)xradius;
        ypixels = originy / (float)yradius;
        
        nstart = -15;
        nstop = 15;
        nstep = 0.015625;
        
        tstart = -15;
        tstop = 15;
        tstep = 0.125;
        steplength = 10;
        
        doCircle = false;
        doGrids = true;
        
        bg = Color.BLACK;
        circle = Color.WHITE;
        grid = Color.WHITE;
        shape = Color.MAGENTA;
        
        time = tstart;
        
        setTitle("Graph");
        setSize(width, height);
        setResizable(false);
        setBackground(bg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
    }
    
    public void setFunctions(String[] tokenX, String[] tokenY, FunctionList usingX, FunctionList usingY) {
    	xfunction = new ExpressionTree(tokenX, usingX);
        yfunction = new ExpressionTree(tokenY, usingY);
        
        xfunction.computeConstants();
        yfunction.computeConstants();
    }
    
    public void setGridlines(String[] tokenXrad, String[] tokenYrad,
    						String[] tokenXscl, String[] tokenYscl,
    						FunctionList using) {
    	ExpressionTree xRad = new ExpressionTree(tokenXrad, using);
    	ExpressionTree yRad = new ExpressionTree(tokenYrad, using);
    	ExpressionTree xScl = new ExpressionTree(tokenXscl, using);
    	ExpressionTree yScl = new ExpressionTree(tokenYscl, using);
    	
    	xRad.computeConstants();
    	yRad.computeConstants();
    	xScl.computeConstants();
    	yScl.computeConstants();
    	
    	xradius = xRad.getValue();
    	yradius = yRad.getValue();
    	xscale = xScl.getValue();
    	yscale = yScl.getValue();
    	
    	xpixels = originx / (float)xradius;
        ypixels = originy / (float)yradius;
    }
    
    public void setRangeN(String[] tokenStart, String[] tokenStop,
    					String[] tokenStep, FunctionList using) {
    	ExpressionTree start = new ExpressionTree(tokenStart, using);
    	ExpressionTree stop = new ExpressionTree(tokenStop, using);
    	ExpressionTree step = new ExpressionTree(tokenStep, using);
    	
    	start.computeConstants();
    	stop.computeConstants();
    	step.computeConstants();
    	
    	nstart = start.getValue();
    	nstop = stop.getValue();
    	nstep = step.getValue();
    }
    
    public void setRangeT(String[] tokenStart, String[] tokenStop,
						String[] tokenStep, FunctionList using) {
    	ExpressionTree start = new ExpressionTree(tokenStart, using);
    	ExpressionTree stop = new ExpressionTree(tokenStop, using);
    	ExpressionTree step = new ExpressionTree(tokenStep, using);
    	
    	start.computeConstants();
    	stop.computeConstants();
    	step.computeConstants();
    	
    	tstart = start.getValue();
    	tstop = stop.getValue();
    	tstep = step.getValue();
    	time = tstart;
    }
    
    public void setCircleRadius(String[] tokenRadius, FunctionList using) {
    	ExpressionTree radius = new ExpressionTree(tokenRadius, using);
    	radius.computeConstants();
    	circleradius = radius.getValue();
    }
    
    public void setStepLength(long time) {
    	steplength = time;
    }
    
    public void setDoGridlines(boolean value) {
    	doGrids = value;
    }
    
    public void setDoCircle(boolean value) {
    	doCircle = value;
    }
    
    public void setBackgroundColor(Color c) {
    	bg = c;
    }
    
    public void setCircleColor(Color c) {
    	circle = c;
    }
    
    public void setGridlineColor(Color c) {
    	grid = c;
    }
    
    public void setShapeColor(Color c) {
    	shape = c;
    }
    
    @Override
    public void paint(Graphics g) {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbGraphics = dbImage.getGraphics();
        paintComponents(dbGraphics);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    @Override
    public void paintComponents(Graphics g) {
    	if(doCircle) {
    		drawCircle(g);
    	}
        
    	if(doGrids) {
    		drawAxes(g);
    		drawGridlines(g);
    	}
        
        if(xfunction != null && yfunction != null) {
        	drawShape(g);
        }
        
        // update t
        if ((time < tstop && tstep>0) || (time > tstop && tstep<0)) {
            time += tstep;
        } else {
            time = tstart;
        }
        
        // thread sleep
        try {
            Thread.sleep(steplength);
        } catch (InterruptedException ex) {
            Logger.getLogger(GeoGrapher.class.getName()).log(Level.SEVERE, null, ex);
        }
        repaint();
    }
    
    //(5/Math.cos(44*t+0.25*time)*Math.cos(t+0.25*time));
    //(5/Math.cos(44*t-0.25*time)*Math.sin(t-0.25*time));
    
    //((5+5*Math.cos(t+time))*Math.cos(t));
    //((5+5*Math.cos(t-time))*Math.sin(t));
    
    //(10*Math.cos(60*t+time)*Math.cos(t)); // high res
    //(10*Math.cos(60*t-time)*Math.sin(t));
    
    //((9+2*Math.cos(time))*Math.cos(60*t)*Math.cos(t-0.5*Math.PI-0.05*time)); //high res
    //((9+2*Math.sin(time))*Math.cos(60*t-0.5*Math.PI)*Math.sin(t-0.05*time));
    
    // (t*Math.cos(time)+t*Math.cos(time+0.5*Math.PI));
    // (t*Math.sin(time)+t*Math.sin(time+0.5*Math.PI));
    
    // (5/Math.cos(22*t-0.25*time)*Math.cos(t+0.25*time));
    // (5/Math.cos(22*t-0.25*time)*Math.sin(t+0.25*time));
    
    public void drawAxes(Graphics g) {
        g.setColor(grid);
        g.drawLine(originx, 0, originx, 2*originy);
        g.drawLine(0, originy, 2*originx, originy);
    }
    
    public void drawGridlines(Graphics g) {
        g.setColor(grid);
        for(double n = xpixels*xscale; n < getWidth() / 2; n += xpixels * xscale) {
            g.drawLine(originx + (int)Math.round(n),
                       originy + Math.round(ypixels/5),
                       originx + (int)Math.round(n),
                       originy - Math.round(ypixels/5));
        }
        for(double n = ypixels*yscale; n < getHeight() / 2; n += ypixels * yscale) {
            g.drawLine(originx + Math.round(xpixels/5),
                       originy + (int)Math.round(n),
                       originx - Math.round(xpixels/5),
                       originy + (int)Math.round(n));
        }
        for(double n = xpixels*xscale; n > -1 * getWidth() / 2; n -= xpixels * xscale) {
            g.drawLine(originx + (int)Math.round(n),
                       originy + Math.round(ypixels/5),
                       originx + (int)Math.round(n),
                       originy - Math.round(ypixels/5));
        }
        for(double n = ypixels*yscale; n > -1 * getHeight() / 2; n -= ypixels * yscale) {
            g.drawLine(originx + Math.round(xpixels/5),
                       originy + (int)Math.round(n),
                       originx - Math.round(xpixels/5),
                       originy + (int)Math.round(n));
        }
    }
    
    public void drawCircle(Graphics g) {
        g.setColor(circle);
        g.drawOval((int)Math.round(originx - (xpixels * circleradius)),
                   (int)Math.round(originy - (ypixels * circleradius)),
                   (int)Math.round(2 * circleradius * xpixels),
                   (int)Math.round(2 * circleradius * ypixels));
    }
    
    public void drawShape(Graphics g) {
        g.setColor(shape);
        
        // plug-in time
        xfunction.plugInT(time); 
        yfunction.plugInT(time);
        
        double xlast, ylast, xnext, ynext;
        int x1,y1,x2,y2;
        
        // initialize
        xfunction.plugInN(nstart);
        yfunction.plugInN(nstart);
        xnext = xfunction.getValue();
        ynext = yfunction.getValue();
        
        for (n = nstart+nstep; n < nstop; n += nstep) {
            xlast = xnext;
            ylast = ynext;
            
            xfunction.plugInN(n);
            yfunction.plugInN(n);
            xnext = xfunction.getValue();
            ynext = yfunction.getValue();
            
            if(!Double.isNaN(xlast) && !Double.isNaN(ylast) &&
               !Double.isNaN(xnext) && !Double.isNaN(ynext)) {
            	x1 = (int)Math.round(originx + xlast * xpixels);
	        	y1 = (int)Math.round(originy - ylast * ypixels);
	        	x2 = (int)Math.round(originx + xnext * xpixels);
	        	y2 = (int)Math.round(originy - ynext * ypixels);
	            
	            if (Math.abs(x2-x1) <= getWidth() && Math.abs(y2-y1) <= getHeight()) {
                    g.drawLine(x1, y1, x2, y2);
	            }
            }
        }
    }
}